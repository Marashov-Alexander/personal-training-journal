package ru.ok.technopolis.training.personal.fragments

import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_view_author.view.*
import kotlinx.android.synthetic.main.item_profile.view.*
import kotlinx.android.synthetic.main.item_train_ex_switcher.*
import kotlinx.android.synthetic.main.item_train_ex_switcher.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository.currentUser
import ru.ok.technopolis.training.personal.utils.logger.Logger
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortExerciseListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import ru.ok.technopolis.training.personal.viewholders.ShortExerciseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ShortWorkoutViewHolder
import java.sql.Time

class ViewAuthorFragment :UserFragment() {
    private var profileNameAndIcon: View? = null
    private var subscribersNumber: TextView? = null
    private var subscriptionsNumber: TextView? = null
    private var trainSwitcher: View? = null
    private var sendMessage: CardView? = null

    private var recyclerView: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var exerciseMutableList = mutableListOf<ShortExerciseItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileNameAndIcon = view.profile
        subscribersNumber = view.subscribers_number
        subscriptionsNumber = view.subscriptions_number
        sendMessage = view.send_author_message
        setHasOptionsMenu(true)
        recyclerView = view.author_tr_ex_list
        val trSwLine = view.train_switch_line
        val exSwitchLine = view.ex_switch_line

        //TODO:Get chat id for the author from db of create new one
        pushExercise(0, "Мое упражнение", "Кардио", "Офп", 0, 0.0)
        pushExercise(1, "Бег на месте", "Кардио", "Офп", 0, 0.0)
        pushExercise(2, "Приседания", "Силовые", "Базовые", 3, 5.0)

        val text = getString(R.string.ex_switcher_text) + " (" + exerciseMutableList.size + ")"
        trainSwitcher?.ex_switch_button?.text = text

        trainSwitcher = view.switcher
        val flag = true
        val id = (activity?.intent?.extras?.get(Page.AUTHOR_ID_KEY)) as Long
        loadAuthorAndSportsInfo(id) { author, list ->
            var sportsList = ""
            for (sport in list) {
                sportsList += if (sport != list.last()) {
                    "${sport.name}, "
                } else {
                    "${sport.name} "
                }
            }
            profileNameAndIcon?.profile_name?.text = author.name
            profileNameAndIcon?.complaint?.visibility = View.VISIBLE
            profileNameAndIcon?.profile_description?.text = sportsList
            subscribersNumber?.text = author.subscribersNumber.toString()
            subscriptionsNumber?.text = author.subscriptionsNumber.toString()
        }
            getUserWorkouts(id) { workouts ->
                 workoutsMutableList = workouts
                loadWorkoutItems(id)
                setButtonsLogic(id, flag, trSwLine, exSwitchLine)
                sendMessage?.setOnClickListener {
                    router?.showChatPage(id)
                }
            }
    }



    private fun loadItems(id: Long, flag: Boolean) {
        clearRecView(flag)
        if (flag) {
            loadWorkoutItems(id)
        } else {
            loadExItems()
        }
    }

    private fun clearRecView(flag: Boolean) {
        if (flag) {
            val listSize = workoutsMutableList.size
            workoutsMutableList.clear()
            recyclerView?.adapter?.notifyItemRangeRemoved(0, listSize)
        } else {
            val listSize = exerciseMutableList.size
            exerciseMutableList.clear()
            recyclerView?.adapter?.notifyItemRangeRemoved(0, listSize)
        }
    }

    private fun loadWorkoutItems(id: Long) {
        getUserWorkouts(id) { workouts ->
            workoutsMutableList = workouts
            exDummyToRecView()
        }
    }

    private fun exDummyToRecView() {

        val text = getString(R.string.training_switcher_text) + " (" + workoutsMutableList.size + ")"
        trainSwitcher?.train_switch_button?.text = text
        val workoutsList = ItemsList(workoutsMutableList)
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router?.showWorkoutPage(workoutItem.id.toLong())
                }
        )
        recyclerView?.adapter = workoutsAdapter
        val workoutsLayoutManager = GridLayoutManager(activity, 2)
        recyclerView?.layoutManager = workoutsLayoutManager
    }

    private fun exerciseDummyAll() {
        pushExercise(0, "Мое упражнение", "Кардио", "Офп", 0, 0.0)
        pushExercise(1, "Бег на месте", "Кардио", "Офп", 0, 0.0)
        pushExercise(2, "Приседания", "Силовые", "Базовые", 3, 5.0)
        val exList = ItemsList(exerciseMutableList)
        val text = getString(R.string.ex_switcher_text) + " (" + exerciseMutableList.size + ")"
        trainSwitcher?.ex_switch_button?.text = text
        val exAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercice,
                dataSource = exList,
                onClick = { exItem -> println("workout ${exItem.id} clicked") },
                onStart = { exItem ->
                    println("workout ${exItem.id} started")
                    router?.showExercisePage(exItem.id.toLong())
                }
        )
        recyclerView?.adapter = exAdapter
        val exLayoutManager = GridLayoutManager(activity, 2)
        recyclerView?.layoutManager = exLayoutManager
    }

    private fun loadExItems() {
        exerciseDummyAll()
    }

    private fun pushExercise(id: Int, name: String, category: String, description: String, sharedNumber: Int, rank: Double) {
        exerciseMutableList.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description,  sharedNumber, rank)
        )
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Logger.d(this, "onCreateOptionsMenu")
        inflater.inflate(R.menu.subscribe_menu, menu)

        val button: MenuItem = menu.findItem(R.id.subscribe_button)

//        if (author?.id in currentUser.subscribers) {
//            button.setIcon(R.drawable.ic_persone_added_)
//        }  else if (author?.id in currentUser.weithingList) {
//            button.setIcon(R.drawable.ic_waiting_approval)
//        }

        button.setOnMenuItemClickListener {
//            if (author?.id not in currentUser.subscribers) {
            button.setIcon(R.drawable.ic_waiting_approval)
//            var chatId = db.createChat()
//            router?.showChatPage(123)
            true
        }

    }
    private fun setButtonsLogic(id: Long, flag: Boolean, trSwLine: View, exSwitchLine: View) {
        var flag1 = flag
        val clL = View.OnClickListener { elem ->
            flag1 = !flag1
            print(flag1)
            if (flag1) {
                if (elem.id == R.id.train_switch_button) {
                    elem.train_switch_button.setTextColor(Color.rgb(24, 120, 103))
                    trSwLine.setBackgroundResource(R.color.design_default_color_secondary_variant)
                    trainSwitcher!!.ex_switch_button.setTextColor(Color.rgb(119, 119, 119))
                    exSwitchLine.setBackgroundResource(R.color.gray_4)
                }

            } else {
                if (elem.id == R.id.train_switch_button) {
                    elem.train_switch_button.setTextColor(Color.rgb(119, 119, 119))
                    trSwLine.setBackgroundResource(R.color.gray_4)
                    trainSwitcher!!.ex_switch_button.setTextColor(Color.rgb(24, 120, 103))
                    exSwitchLine.setBackgroundResource(R.color.design_default_color_secondary_variant)

                }
            }
            loadItems(id, flag1)
        }

        val exCll = View.OnClickListener { elem ->
            flag1 = !flag1
            if (!flag1) {
                if (elem.id == R.id.ex_switch_button) {
                    elem.ex_switch_button.setTextColor(Color.rgb(24, 120, 103))
                    exSwitchLine.setBackgroundResource(R.color.design_default_color_secondary_variant)
                    trainSwitcher!!.train_switch_button.setTextColor(Color.rgb(119, 119, 119))
                    train_switch_line.setBackgroundResource(R.color.gray_4)
                }

            } else {
                if (elem.id == R.id.ex_switch_button) {
                    elem.ex_switch_button.setTextColor(Color.rgb(119, 119, 119))
                    exSwitchLine.setBackgroundResource(R.color.gray_4)
                    trainSwitcher!!.train_switch_button.setTextColor(Color.rgb(24, 120, 103))
                    train_switch_line.setBackgroundResource(R.color.design_default_color_secondary_variant)
                }
            }
            loadItems(id, flag1)
        }

        trainSwitcher!!.train_switch_button.setOnClickListener(clL)
        trainSwitcher!!.ex_switch_button.setOnClickListener(exCll)
    }

    override fun getFragmentLayoutId() = R.layout.fragment_view_author

}