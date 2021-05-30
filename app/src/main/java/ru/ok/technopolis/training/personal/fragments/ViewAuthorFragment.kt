package ru.ok.technopolis.training.personal.fragments

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
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.logger.Logger
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortExerciseListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import ru.ok.technopolis.training.personal.viewholders.ShortExerciseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ShortWorkoutViewHolder

class ViewAuthorFragment : UserFragment() {
    private var profileNameAndIcon: View? = null
    private var subscribersNumber: TextView? = null
    private var subscriptionsNumber: TextView? = null
    private var trainSwitcher: View? = null
    private var sendMessage: CardView? = null

    private var recyclerView: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var exerciseMutableList = mutableListOf<ShortExerciseItem>()

    private var authorId: Long = -1L

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
        trainSwitcher = view.switcher


        val id = (activity?.intent?.extras?.get(Page.AUTHOR_ID_KEY)) as Long
        authorId = id
        authorLayout(id)

        val flag = true
        setExercisesNumber(id)
        getUserWorkouts(id) { workouts ->
            workoutsMutableList = workouts
            loadItems(id, flag)
        }
        setButtonsLogic(id, trSwLine, exSwitchLine)

        //TODO:Get chat id for the author from db of create new one
        sendMessage?.setOnClickListener {
            router?.showChatPage(id, null)
        }
    }

    private fun setExercisesNumber(id: Long) {
        getUserExercises(id) { exercises ->
            val text = getString(R.string.ex_switcher_text) + " (" + exercises.size + ")"
            trainSwitcher?.ex_switch_button?.text = text
            exerciseMutableList = exercises
        }
    }

    private fun authorLayout(id: Long) {
        loadAuthorAndSportsInfo(id) { author, list ->
            activity?.base_toolbar?.title = author.name
            profileNameAndIcon?.profile_name?.text = author.name
            profileNameAndIcon?.complaint?.visibility = View.VISIBLE
            profileNameAndIcon?.profile_description?.text = makeSportsList(list)
            subscribersNumber?.text = author.subscribersNumber.toString()
            subscriptionsNumber?.text = author.subscriptionsNumber.toString()
        }
    }

    private fun makeSportsList(list: List<WorkoutSportEntity>): String {
        var sportsList = ""
        for (sport in list) {
            sportsList += if (sport != list.last()) {
                "${sport.name}, "
            } else {
                "${sport.name} "
            }
        }
        return sportsList
    }

    private fun loadItems(id: Long, flag: Boolean) {
        clearRecView(flag)
        if (flag) {
            loadWorkouts(id)
        } else {
            loadExercises(id)
        }
    }

    private fun clearRecView(flag: Boolean) {
        val listSize: Int
        if (flag) {
            listSize = workoutsMutableList.size
            workoutsMutableList.clear()
        } else {
            listSize = exerciseMutableList.size
            exerciseMutableList.clear()
        }
        recyclerView?.adapter?.notifyItemRangeRemoved(0, listSize)
    }

    private fun loadWorkouts(id: Long) {
        getUserWorkouts(id) { workouts ->
            val text = getString(R.string.training_switcher_text) + " (" + workouts.size + ")"
            trainSwitcher?.train_switch_button?.text = text
            val workoutsList = ItemsList(workouts)
            val workoutsAdapter = ShortWorkoutListAdapter(
                    holderType = ShortWorkoutViewHolder::class,
                    layoutId = R.layout.item_short_workout,
                    dataSource = workoutsList,
                    onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
                    onStart = { workoutItem ->
                        println("workout ${workoutItem.id} started")
                        router?.showWorkoutPage(workoutItem.workout.id)
                    }
            )
            recyclerView?.adapter = workoutsAdapter
            val workoutsLayoutManager = GridLayoutManager(activity, 2)
            recyclerView?.layoutManager = workoutsLayoutManager
        }
    }

    private fun loadExercises(id: Long) {
        getUserExercises(id) { exercises ->
            val text = getString(R.string.ex_switcher_text) + " (" + exercises.size + ")"
            trainSwitcher?.ex_switch_button?.text = text
            val exList = ItemsList(exercises)
            val exAdapter = ShortExerciseListAdapter(
                    holderType = ShortExerciseViewHolder::class,
                    layoutId = R.layout.item_short_exercise,
                    dataSource = exList,
                    onClick = { exItem -> println("workout ${exItem.id} clicked") },
                    onStart = { exItem ->
                        println("workout ${exItem.exercise.id} started")
                        router?.showExercisePage(exItem.exercise.id)
                    }
            )
            recyclerView?.adapter = exAdapter
            val exLayoutManager = GridLayoutManager(activity, 2)
            recyclerView?.layoutManager = exLayoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Logger.d(this, "onCreateOptionsMenu")
        inflater.inflate(R.menu.subscribe_menu, menu)

        val button: MenuItem = menu.findItem(R.id.subscribe_button)
        val userId = CurrentUserRepository.currentUser.value?.id
        var falg = false
        getUserSubscribtionsIdList(userId!!) { subscribers ->
            if (authorId in subscribers) {
                button.setIcon(R.drawable.ic_person_added)
            } else {
                button.setIcon(R.drawable.ic_baseline_person_add_24)
            }

            button.setOnMenuItemClickListener {
                if (authorId !in subscribers) {
                    button.setIcon(R.drawable.ic_waiting_approval)
                   subscribeToAuthor(userId, authorId) { id ->
                       println("llllllllllllllllllllllll ${id}")
                  }
                }
                    true
            }
        }
    }

    private fun setButtonsLogic(id: Long, trSwLine: View, exSwitchLine: View) {
        var flag1 = true
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