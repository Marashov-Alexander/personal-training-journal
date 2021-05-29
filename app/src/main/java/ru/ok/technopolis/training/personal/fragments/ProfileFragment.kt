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
import java.sql.Time


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

        //TODO: change to real


        val id = CurrentUserRepository.currentUser.value?.id
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
            val prof = ProfileItem(id.toString(),
                    id,
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

            val flag = true
            putNumbers(flag)
            exDummyToRecView()
            val sharedFlag = false
            val privateFlag = false
            getUserWorkouts(id) { workouts ->
                workoutsMutableList = workouts
//                loadWorkoutItems(id)
                setButtonslogic(sharedFlag, privateFlag, flag, trSwLine, exSwitchLine)
            }

        }
    }


    private fun loadItems(flag: Boolean, privateFlag: Boolean, sharedFlag: Boolean) {
       clearRecView(flag)
        if (flag) {
            loadWorkoutItems(privateFlag, sharedFlag)
        } else {
            loadExItems(privateFlag, sharedFlag)
        }
    }

    private fun loadWorkoutItems(privateFlag: Boolean, sharedFlag: Boolean) {
        when {
            privateFlag -> {

                exDummyPrivate()
            }
            sharedFlag -> {
                exDummyShared()
            }
            else -> {
                exDummyToRecView()
            }
        }
    }
    private fun loadExItems(privateFlag: Boolean, sharedFlag: Boolean) {
        when {
            privateFlag -> {
                exerciseDummyPrivate()
            }
            sharedFlag -> {
               exerciseDummyShared()
            }
            else -> {
               exerciseDummyAll()
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

    private fun exDummyToRecView() {
        pushWorkoutShared()
        pushWorkoutPrivate()
        val workoutsList = ItemsList(workoutsMutableList)
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")
                router?.showWorkoutPage(workoutItem.id.toLong())
                },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router?.showWorkoutPage(workoutItem.id.toLong())
                }
        )
        recyclerView?.adapter = workoutsAdapter
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = workoutsLayoutManager
    }

    private fun exDummyShared() {
        pushWorkoutShared()
        val workoutsList = ItemsList(workoutsMutableList)
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")},
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router?.showWorkoutPage(workoutItem.id.toLong())
                }
        )
        recyclerView?.adapter = workoutsAdapter
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = workoutsLayoutManager
    }

    private fun pushWorkoutShared(): Int {
        pushWorkout(0, "Тренировка 1", "Кардио", "", "Легкая атлетика", 123, 4.0)
        pushWorkout(1, "Тренировка 2", "Силовая", "", "Легкая атлетика", 200, 3.5)
        pushWorkout(1, "Тренировка 3", "Силовая", "", "Легкая атлетика", 240, 4.5)
        return workoutsMutableList.size
    }

    private fun exDummyPrivate() {
        pushWorkoutPrivate()
        val workoutsList = ItemsList(workoutsMutableList)
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")},
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router?.showWorkoutPage(workoutItem.id.toLong())
                }
        )
        recyclerView?.adapter = workoutsAdapter
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = workoutsLayoutManager
    }

    private fun pushWorkoutPrivate():Int {
        pushWorkout(0, "Любимая тренировка", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout(1, "Тренировка 1", "Силовая", "", "Легкая атлетика", 0, 0.0)
        return workoutsMutableList.size
    }

    private fun exerciseDummyPrivate() {
        pushExPrivate()
        val exList = ItemsList(exerciseMutableList)
        val exAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercice,
                dataSource = exList,
                onClick = {exItem -> println("workout ${exItem.id} clicked")},
                onStart = { exItem ->
                    println("workout ${exItem.id} started")
                    router?.showExercisePage(exItem.id.toLong())
                }
        )
        recyclerView?.adapter = exAdapter
        val exLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = exLayoutManager
    }

    private fun exerciseDummyShared() {
        pushExShared()
        val exList = ItemsList(exerciseMutableList)
        val exAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercice,
                dataSource = exList,
                onClick = {exItem -> println("workout ${exItem.id} clicked")},
                onStart = { exItem ->
                    println("workout ${exItem.id} started")
                    router?.showExercisePage(exItem.id.toLong())
                }
        )
        recyclerView?.adapter = exAdapter
        val exLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = exLayoutManager
    }
    private fun exerciseDummyAll() {
        pushExPrivate()
        pushExShared()
        val exList = ItemsList(exerciseMutableList)
        val exAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercice,
                dataSource = exList,
                onClick = { exItem -> println("workout ${exItem.id} clicked")},
                onStart = { exItem ->
                    println("workout ${exItem.id} started")
                    router?.showExercisePage(exItem.id.toLong())
                }
        )
        recyclerView?.adapter = exAdapter
        val exLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = exLayoutManager
    }

    private fun putNumbers(flag: Boolean) {
        if (flag) {
            workoutsMutableList.clear()
            val private = pushWorkoutPrivate()
            var text = getString(R.string.private_filter_text) + "(" + private + ")"
            filterButtons?.private_filter_button?.text = text
            workoutsMutableList.clear()
            val shared = pushWorkoutShared()
            workoutsMutableList.clear()
            text = getString(R.string.shared_filter_text) + "(" + shared + ")"
            filterButtons?.shared_filter_button?.text = text
            val all = private + shared
            text = getString(R.string.all_filter_text) + "(" + all + ")"
            filterButtons?.all_filter_button?.text = text
        } else {
            exerciseMutableList.clear()
            val private = pushExPrivate()
            var text = getString(R.string.private_filter_text) + "(" + private + ")"
            filterButtons?.private_filter_button?.text = text
            exerciseMutableList.clear()
            val shared = pushExShared()
            exerciseMutableList.clear()
            text = getString(R.string.shared_filter_text) + "(" + shared + ")"
            filterButtons?.shared_filter_button?.text = text
            val all = private + shared
            text = getString(R.string.all_filter_text) + "(" + all + ")"
            filterButtons?.all_filter_button?.text = text
        }
    }

    private fun pushExPrivate(): Int {
        pushExercise(0, "Мое упражнение", "Кардио", "Офп", 0, 0.0)
        pushExercise(1, "Бег на месте", "Кардио", "Офп", 0, 0.0)
        return exerciseMutableList.size
    }

    private fun pushExShared() :Int {
        pushExercise(2, "Приседания", "Силовые", "Базовые", 3, 5.0)
        return exerciseMutableList.size
    }

    private fun pushWorkout(id: Int, name: String, category: String, description: String, sport: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList.add(
                ShortWorkoutItem(id.toString(),name, category, sport, sharedNumber, rank)
        )
    }

    private fun pushExercise(id: Int, name: String, category: String, description: String,  sharedNumber: Int, rank: Double) {
        exerciseMutableList.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description,  sharedNumber, rank)
        )
    }

    private fun setButtonslogic(sharedFlag: Boolean, privateFlag: Boolean, flag: Boolean, trSwLine: View, exSwitchLine: View) {
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
            loadItems(flag1, privateFlag1, sharedFlag1)
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
            loadItems(flag1, privateFlag1, sharedFlag1)
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
            loadItems(flag1, privateFlag1, sharedFlag1)
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
            putNumbers(flag1)
            loadItems(flag1, privateFlag1, sharedFlag1)
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
            putNumbers(flag1)
            loadItems(flag1, privateFlag1, sharedFlag1)
        }


        filterButtons!!.all_filter_button.setOnClickListener(allCl)
        filterButtons!!.shared_filter_button.setOnClickListener(sharedCl)
        filterButtons!!.private_filter_button.setOnClickListener(privateCl)
        trainSwitcher!!.train_switch_button.setOnClickListener(clL)
        trainSwitcher!!.ex_switch_button.setOnClickListener(exCll)
    }



    override fun getFragmentLayoutId() : Int = R.layout.fragment_profile

}