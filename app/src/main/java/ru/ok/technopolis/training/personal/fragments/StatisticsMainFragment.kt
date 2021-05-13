package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_statistics_main.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortExerciseListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import ru.ok.technopolis.training.personal.viewholders.ShortExerciseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ShortWorkoutViewHolder
import java.sql.Time
import kotlin.math.roundToInt

class StatisticsMainFragment : BaseFragment() {
    private var toggle: View? = null
    private var recyclerView: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var exerciseMutableList = mutableListOf<ShortExerciseItem>()
    private var flag = true
    private var exInitLeft = 0
    private var exInitRight = 0
    private var workoutInitLeft = 0
    private var workoutInitRight = 0
    private var switcherSize = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggle = view.toggle_background
        recyclerView = view.statistics_workout_ex_list
        loadItems(flag)
        var touchMode = 0
        val clickListener = View.OnClickListener {
            toggleSwitch()
        }

        val touchListener = View.OnTouchListener { v, event ->
            val x: Int
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                workoutInitLeft = toggle!!.toggle_workout_side.left
                workoutInitRight= toggle!!.toggle_workout_side.right
                exInitLeft = toggle!!.toggle_ex_side.left
                exInitRight = toggle!!.toggle_ex_side.right
                switcherSize = toggle!!.toggle_switcher.width
                touchMode = 1
            } else if (event.actionMasked == MotionEvent.ACTION_MOVE ) {
                x = event.rawX.roundToInt()
                if (flag and (x > workoutInitRight))
                {
                   toggle!!.toggle_switcher.right = x
                    touchMode = 2

                } else if (!flag and (x < exInitLeft)) {
                    toggle!!.toggle_switcher.left = x
                    touchMode = 3
                }
            }else if (event.actionMasked == MotionEvent.ACTION_UP) {
                when {
                    touchMode == 1 -> {
                        v.performClick()
                    }
                    ((touchMode == 3) and (toggle!!.toggle_switcher.left < workoutInitRight))
                            or ((touchMode == 2) and (toggle!!.toggle_switcher.right > exInitLeft)) -> {
                        toggleSwitch()
                    }
                    ((touchMode == 2) or (touchMode == 3)) -> {
                        setToggleSwitcherPosition()
                    }
                }
            }
            true
        }

        toggle!!.toggle_workout_side.setOnClickListener(clickListener)
        toggle!!.toggle_ex_side.setOnClickListener(clickListener)
        toggle!!.toggle_workout_side.setOnTouchListener(touchListener)
        toggle!!.toggle_ex_side.setOnTouchListener(touchListener)
        toggle!!.toggle_switcher.setOnTouchListener(touchListener)
        toggle!!.toggle_switcher.setOnTouchListener(touchListener)
    }

    private fun toggleSwitch() {
        flag = !flag
        setToggleSwitcherPosition()
        loadItems(flag)
    }

    private fun setToggleSwitcherPosition() {
        if (flag) {
            toggle!!.toggle_switcher.left = workoutInitLeft
            toggle!!.toggle_switcher.right = toggle!!.toggle_switcher.left + switcherSize

        } else {
            toggle!!.toggle_switcher.right = exInitRight
            toggle!!.toggle_switcher.left = exInitRight - switcherSize
        }
    }

    private fun loadItems(flag: Boolean) {
        clearRecView(flag)
        if (flag) {
            exDummyToRecView()
        } else {
            exerciseDummyAll()
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
        for (i in 1..5) pushWorkout(i)
        val workoutsList = ItemsList(workoutsMutableList)
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")},
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                }
        )
        recyclerView?.adapter = workoutsAdapter
        val workoutsLayoutManager = GridLayoutManager(activity, 2)
        recyclerView?.layoutManager = workoutsLayoutManager
    }

    private fun exerciseDummyAll() {
        for (i in 1..2) pushExercise(i)
        val exList = ItemsList(exerciseMutableList)
        val exAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercice,
                dataSource = exList,
                onClick = { exItem -> println("workout ${exItem.id} clicked")},
                onStart = { exItem ->
                    println("workout ${exItem.id} started")
                }
        )
        recyclerView?.adapter = exAdapter
        val exLayoutManager = GridLayoutManager(activity, 2)
        recyclerView?.layoutManager = exLayoutManager
    }
        private fun pushWorkout(id: Int) {
            workoutsMutableList.add(
                    ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), "MYвшпвшпвкпиквпшкивпквпвпквпивчмпч MY", "kardio", "ofp", "", true, 0,0.0, false)
            )
        }

        private fun pushExercise(id: Int) {
            exerciseMutableList.add(
                    ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), "dljgd hdrh rhre", "kardio", "ofp", true, 0, 0.0)
            )
        }
    override fun getFragmentLayoutId(): Int = R.layout.fragment_statistics_main
}
