package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_statistics_main.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import ru.ok.technopolis.training.personal.viewholders.ShortWorkoutViewHolder

class StatisticsMainFragment : UserFragment() {
    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.statistics_workout_ex_list
        activity?.base_toolbar?.title = getString(R.string.statistics)
        loadWorkouts()
    }

    private fun loadWorkouts() {
        val userId = CurrentUserRepository.currentUser.value?.id
        getUserWorkouts(userId!!) { workouts ->
            val workoutsList = ItemsList(workouts)
            val workoutsAdapter = ShortWorkoutListAdapter(
                    holderType = ShortWorkoutViewHolder::class,
                    layoutId = R.layout.item_short_workout,
                    dataSource = workoutsList,
                    onClick = { workoutItem ->
                        println("workout ${workoutItem.id} clicked")
                    },
                    onStart = { workoutItem ->
                        println("workout ${workoutItem.id} started")
                        router?.showWorkoutProgressPage(workoutItem.workout.id)
                    }
            )
            recyclerView?.adapter = workoutsAdapter
            val workoutsLayoutManager = GridLayoutManager(activity, 2)
            recyclerView?.layoutManager = workoutsLayoutManager
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_statistics_main
}
