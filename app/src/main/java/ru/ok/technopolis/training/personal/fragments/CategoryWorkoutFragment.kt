package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import kotlin.random.Random

abstract class CategoryWorkoutFragment : BaseFragment() {
    protected fun loadCategoryWorkouts(
            actionsAfter: (
                    MutableList<CategoryWorkoutsItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val categoryElem = mutableListOf<CategoryWorkoutsItem>()

                val categories = it.workoutCategoryDao().getAll()
                categories.map { category ->
                    val workouts = it.workoutDao().getByCategoryId(category.id)
                    val workoutsList = workouts.map { workout ->
                        //                    val sport = it.workoutSportDao().getById(workout.sportId)
                        val downloadsNumber = 0
                        val rank = 0.0
                        ShortWorkoutItem(
                                Random.nextInt().toString(),
                                workout.name,
                                category.name,
                                workout.sport,
                                downloadsNumber,
                                rank
                        )
                    }.toMutableList()
                    categoryElem.add(CategoryWorkoutsItem(category.id.toString(), category.name, workoutsList))
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(categoryElem)
                }
            }

        }
    }
}
