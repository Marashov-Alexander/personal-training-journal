package ru.ok.technopolis.training.personal.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item_profile.view.*
import kotlinx.android.synthetic.main.item_train_ex_switcher.*
import kotlinx.android.synthetic.main.item_train_ex_switcher.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortExerciseListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import ru.ok.technopolis.training.personal.viewholders.ShortExerciseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ShortWorkoutViewHolder


class ProfileFragment : UserFragment() {
    private var profileNameAndIcon: View? = null
    private var trainSwitcher: View? = null
    private var subscribersNumber: TextView? = null
    private var subscriptionsNumber: TextView? = null
    private var sharedTrainingsNumber: TextView? = null
    private var sharedExercisesNumber: TextView? = null
    private var filterButtons: View? = null

    private var recyclerView: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var exerciseMutableList = mutableListOf<ShortExerciseItem>()
    private var id: Long? = CurrentUserRepository.currentUser.value!!.id

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileNameAndIcon = view.profile
        trainSwitcher = view.switcher
        subscribersNumber = view.subscribers_number
        subscriptionsNumber = view.subscriptions_number
        sharedTrainingsNumber = view.shared_trainings_number
        sharedExercisesNumber = view.shared_ex_number
        filterButtons = view.tr_ex_filter_buttons
        recyclerView = view.profile_tr_ex_list

        activity?.base_toolbar?.title = getString(R.string.profile)
        val trSwLine = view.train_switch_line
        val exSwitchLine = view.ex_switch_line

        id = CurrentUserRepository.currentUser.value?.id
        loadSportsInfo(id!!) { list ->


            val name = CurrentUserRepository.currentUser.value?.firstName + " ${CurrentUserRepository.currentUser.value?.lastName}"
            val picture = CurrentUserRepository.currentUser.value?.pictureUrlStr

            var sportsList = ""
            for (sport in list) {
                sportsList += if (sport != list.last()) {
                    "${sport.name}, "
                } else {
                    "${sport.name} "
                }
            }
            //TODO: change to real
            val prof = ProfileItem(id.toString(),
                    id!!,
                    name,
                    listOf(sportsList),
                    true,
                    picture,
                    0,
                    0,
                    2,
                    3
            )

            profileNameAndIcon?.profile_name?.text = prof.name
            profileNameAndIcon?.complaint?.visibility = View.INVISIBLE
            profileNameAndIcon?.profile_description?.text = sportsList
            subscribersNumber?.text = prof.subscribersNumber.toString()
            subscriptionsNumber?.text = prof.subscriptionsNumber.toString()
            sharedTrainingsNumber?.text = prof.sharedTrainingsNumber.toString()
            sharedExercisesNumber?.text = prof.sharedExercisesNumber.toString()

            subscribersNumber?.setOnClickListener {
                router?.showSubscribersPage(prof.userId)
            }

            subscriptionsNumber?.setOnClickListener {
                router?.showSubscriptionsPage(prof.userId)
            }
        }

        val flag = true
        val sharedFlag = false
        val privateFlag = false
        setNumbers(id!!, flag)
        setButtonsLogic(sharedFlag, privateFlag, flag, trSwLine, exSwitchLine)
    }

    private fun setNumbers(id: Long, flag: Boolean) {
        if (flag) {
            loadItems(flag = true, privateFlag = true, sharedFlag = false, id = id)
            loadItems(flag = true, privateFlag = false, sharedFlag = true, id = id)
            loadItems(flag = true, privateFlag = false, sharedFlag = false, id = id)
        } else {
            loadItems(flag = false, privateFlag = true, sharedFlag = false, id = id)
            loadItems(flag = false, privateFlag = false, sharedFlag = true, id = id)
            loadItems(flag = false, privateFlag = false, sharedFlag = false, id = id)
        }
    }

    private fun loadItems(flag: Boolean, privateFlag: Boolean, sharedFlag: Boolean, id: Long) {
        clearRecView(flag)
        if (flag) {
            loadWorkoutItems(privateFlag, sharedFlag, id)
        } else {
            loadExItems(privateFlag, sharedFlag, id)
        }
    }

    private fun loadWorkoutItems(privateFlag: Boolean, sharedFlag: Boolean, id: Long) {
        when {
            privateFlag -> {
                privateWorkouts(id)

            }
            sharedFlag -> {
                sharedWorkouts(id)
            }
            else -> {
                allWorkouts(id)
            }
        }
    }

    private fun loadExItems(privateFlag: Boolean, sharedFlag: Boolean, id: Long) {
        when {
            privateFlag -> {
                privateExercises(id)
            }
            sharedFlag -> {
                sharedExercises(id)
            }
            else -> {
                allExercises(id)
            }
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

    private fun allWorkouts(id: Long) {
        getUserWorkouts(id) { workouts ->
            workoutsMutableList = workouts
            val all = workoutsMutableList.size
            val text = getString(R.string.all_filter_text) + "(" + all + ")"
            filterButtons?.all_filter_button?.text = text
            loadWorkouts(workouts)
        }
    }

    private fun privateWorkouts(id: Long) {
        getUserWorkoutsPrivate(id) { workouts ->
            workoutsMutableList = workouts
            val private = workoutsMutableList.size
            val text = getString(R.string.private_filter_text) + "(" + private + ")"
            filterButtons?.private_filter_button?.text = text
            loadWorkouts(workouts)
        }
    }

    private fun sharedWorkouts(id: Long) {
        getUserWorkoutsShared(id) { workouts ->
            workoutsMutableList = workouts
            val shared = workoutsMutableList.size
            val text = getString(R.string.shared_filter_text) + "(" + shared + ")"
            filterButtons?.shared_filter_button?.text = text
            loadWorkouts(workouts)
        }
    }

    private fun allExercises(id: Long) {
        getUserExercises(id) { workouts ->
            exerciseMutableList = workouts
            val all = exerciseMutableList.size
            val text = getString(R.string.all_filter_text) + "(" + all + ")"
            filterButtons?.all_filter_button?.text = text
            loadExercises(workouts)
        }
    }

    private fun privateExercises(id: Long) {
        getUserExercisesPrivate(id) { workouts ->
            exerciseMutableList = workouts
            loadExercises(workouts)
            val private = exerciseMutableList.size
            val text = getString(R.string.private_filter_text) + "(" + private + ")"
            filterButtons?.private_filter_button?.text = text
        }
    }

    private fun sharedExercises(id: Long) {
        getUserExercisesShared(id) { workouts ->
            exerciseMutableList = workouts
            val shared = exerciseMutableList.size
            val text = getString(R.string.shared_filter_text) + "(" + shared + ")"
            filterButtons?.shared_filter_button?.text = text
            loadExercises(workouts)
        }
    }

    private fun loadWorkouts(workouts: MutableList<ShortWorkoutItem>) {
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
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = workoutsLayoutManager
    }

    private fun loadExercises(exercises: MutableList<ShortExerciseItem>) {
        val exList = ItemsList(exercises)
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
        val exLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = exLayoutManager
    }


    private fun setButtonsLogic(sharedFlag: Boolean, privateFlag: Boolean, flag: Boolean, trSwLine: View, exSwitchLine: View) {
        var sharedFlag1 = sharedFlag
        var privateFlag1 = privateFlag
        var flag1 = flag
        val allCl = View.OnClickListener {
            filterButtons!!.all_filter_button.setBackgroundResource(R.drawable.border_button_selected)
            filterButtons!!.all_filter_button.setTextColor(Color.rgb(24, 120, 103))
            filterButtons!!.shared_filter_button.setBackgroundResource(R.drawable.border_button)
            filterButtons!!.shared_filter_button.setTextColor(Color.rgb(119, 119, 119))
            filterButtons!!.private_filter_button.setBackgroundResource(R.drawable.border_button)
            filterButtons!!.private_filter_button.setTextColor(Color.rgb(119, 119, 119))
            sharedFlag1 = false
            privateFlag1 = false
            loadItems(flag1, privateFlag1, sharedFlag1, id!!)
        }

        val sharedCl = View.OnClickListener { elem ->
            sharedFlag1 = !sharedFlag1
            if (sharedFlag1) {
                elem.setBackgroundResource(R.drawable.border_button_selected)
                elem.shared_filter_button.setTextColor(Color.rgb(24, 120, 103))
                filterButtons!!.private_filter_button.setBackgroundResource(R.drawable.border_button)
                filterButtons!!.private_filter_button.setTextColor(Color.rgb(119, 119, 119))
                filterButtons!!.all_filter_button.setBackgroundResource(R.drawable.border_button)
                filterButtons!!.all_filter_button.setTextColor(Color.rgb(119, 119, 119))
                privateFlag1 = false
            } else {
                elem.setBackgroundResource(R.drawable.border_button)
                elem.shared_filter_button.setTextColor(Color.rgb(119, 119, 119))
                filterButtons!!.private_filter_button.setBackgroundResource(R.drawable.border_button)
                filterButtons!!.private_filter_button.setTextColor(Color.rgb(119, 119, 119))
                filterButtons!!.all_filter_button.setBackgroundResource(R.drawable.border_button_selected)
                filterButtons!!.all_filter_button.setTextColor(Color.rgb(24, 120, 103))
            }
            loadItems(flag1, privateFlag1, sharedFlag1, id!!)
        }

        val privateCl = View.OnClickListener { elem ->
            privateFlag1 = !privateFlag1
            if (privateFlag1) {
                elem.setBackgroundResource(R.drawable.border_button_selected)
                elem.private_filter_button.setTextColor(Color.rgb(24, 120, 103))
                filterButtons!!.shared_filter_button.setBackgroundResource(R.drawable.border_button)
                filterButtons!!.shared_filter_button.setTextColor(Color.rgb(119, 119, 119))
                filterButtons!!.all_filter_button.setBackgroundResource(R.drawable.border_button)
                filterButtons!!.all_filter_button.setTextColor(Color.rgb(119, 119, 119))
                sharedFlag1 = false
            } else {
                elem.setBackgroundResource(R.drawable.border_button)
                elem.private_filter_button.setTextColor(Color.rgb(119, 119, 119))
                filterButtons!!.shared_filter_button.setBackgroundResource(R.drawable.border_button)
                filterButtons!!.shared_filter_button.setTextColor(Color.rgb(119, 119, 119))
                filterButtons!!.all_filter_button.setBackgroundResource(R.drawable.border_button_selected)
                filterButtons!!.all_filter_button.setTextColor(Color.rgb(24, 120, 103))
            }
            loadItems(flag1, privateFlag1, sharedFlag1, id!!)
        }

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
            setNumbers(id!!, flag1)
            loadItems(flag1, privateFlag1, sharedFlag1, id!!)
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
            setNumbers(id!!, flag1)
            loadItems(flag1, privateFlag1, sharedFlag1, id!!)
        }

        filterButtons!!.all_filter_button.setOnClickListener(allCl)
        filterButtons!!.shared_filter_button.setOnClickListener(sharedCl)
        filterButtons!!.private_filter_button.setOnClickListener(privateCl)
        trainSwitcher!!.train_switch_button.setOnClickListener(clL)
        trainSwitcher!!.ex_switch_button.setOnClickListener(exCll)
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_profile
}