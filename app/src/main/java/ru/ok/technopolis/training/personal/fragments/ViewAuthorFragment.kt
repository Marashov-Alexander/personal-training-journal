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

class ViewAuthorFragment : BaseFragment() {
    private var profileNameAndIcon: View? = null
    private var subscribersNumber: TextView? = null
    private var subscriptionsNumber: TextView? = null
    private var trainSwitcher: View? = null
    private var sendMessage: CardView? = null

    private var recyclerView: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var exerciseMutableList = mutableListOf<ShortExerciseItem>()

    private var author: UserEntity? = null

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
        sendMessage?.setOnClickListener {
            router?.showChatPage(123)
        }

        trainSwitcher = view.switcher
        var flag = true
        loadWorkoutItems()

        val clL = View.OnClickListener { elem ->
            flag = !flag
            print(flag)
            if (flag) {
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
            loadItems(flag)
        }

        val exCll = View.OnClickListener { elem ->
            flag = !flag
            if (!flag) {
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
            loadItems(flag)
        }

        trainSwitcher!!.train_switch_button.setOnClickListener(clL)
        trainSwitcher!!.ex_switch_button.setOnClickListener(exCll)
        pushExercise(0, "Мое упражнение", "Кардио", "Офп", 0, 0.0)
        pushExercise(1, "Бег на месте", "Кардио", "Офп", 0, 0.0)
        pushExercise(2, "Приседания", "Силовые", "Базовые", 3, 5.0)
        val text = getString(R.string.ex_switcher_text) + " (" + exerciseMutableList.size + ")"
        trainSwitcher?.ex_switch_button?.text = text

        val list = listOf("Легкая атлетика", "Бейсбол", "Теннис")
        val authorId = (activity?.intent?.extras?.get(Page.AUTHOR_ID_KEY)) as Long
        val prof = ProfileItem("1234", authorId, "Иванов Иван", list, true, null, 5, 10, 23, 6)

        author = UserEntity(prof.name, prof.name, prof.name, "123", "f", "", -1, prof.userId)
        activity?.base_toolbar?.title = getString(R.string.author) + " ${prof.name}"

        var sportsList = ""
        for (sport in prof.sports!!) {
            sportsList += if (sport != prof.sports.last()) {
                "$sport, "
            } else {
                "$sport "
            }
        }
        profileNameAndIcon?.profile_name?.text = prof.name
        profileNameAndIcon?.complaint?.visibility = View.VISIBLE
        profileNameAndIcon?.profile_description?.text = sportsList
        subscribersNumber?.text = prof.subscribersNumber.toString()
        subscriptionsNumber?.text = prof.subscriptionsNumber.toString()
    }

    private fun loadItems(flag: Boolean) {
        clearRecView(flag)
        if (flag) {
            loadWorkoutItems()
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

    private fun loadWorkoutItems() {
        exDummyToRecView()
    }

    private fun exDummyToRecView() {
        pushWorkout(0, "Тренировка 1", "Кардио", "", "Легкая атлетика", 123, 4.0)
        pushWorkout(1, "Тренировка 2", "Силовая", "", "Легкая атлетика", 200, 3.5)
        pushWorkout(1, "Тренировка 3", "Силовая", "", "Легкая атлетика", 240, 4.5)
        pushWorkout(0, "Любимая тренировка", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout(1, "Тренировка 1", "Силовая", "", "Легкая атлетика", 0, 0.0)
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

    private fun pushWorkout(id: Int, name: String, category: String, description: String, sport: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList.add(
                ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), name, "lsl", category, sport, "40 min", true, sharedNumber, rank, false, false)
        )
    }

    private fun pushExercise(id: Int, name: String, category: String, description: String, sharedNumber: Int, rank: Double) {
        exerciseMutableList.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description, true, sharedNumber, rank)
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

    override fun getFragmentLayoutId() = R.layout.fragment_view_author

}