package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_statistics_main.view.*
import kotlinx.android.synthetic.main.view_appbar.*
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
    private var recyclerView: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.statistics_workout_ex_list
        activity?.base_toolbar?.title = getString(R.string.statistics)
        exDummyToRecView()
    }

    private fun exDummyToRecView() {
        for (i in 1..5) pushWorkout(i)
        val workoutsList = ItemsList(workoutsMutableList)
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = { workoutItem ->
                    println("workout ${workoutItem.id} clicked")
                },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router?.showWorkoutProgressPage()
                }
        )
        recyclerView?.adapter = workoutsAdapter
        val workoutsLayoutManager = GridLayoutManager(activity, 2)
        recyclerView?.layoutManager = workoutsLayoutManager
    }


    private fun pushWorkout(id: Int) {
        workoutsMutableList.add(
                ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), "MYвшпвшпвкпиквпшкивпквпвпквпивчмпч MY", "kardio", "ofp", "", true, 0, 0.0, false, false)
        )
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_statistics_main
}
