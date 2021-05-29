package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import kotlin.random.Random

abstract class CategoryWorkoutFragment : BaseFragment() {
    protected fun loadCategoryWorkouts( userId: Long, library: Boolean,
            actionsAfter: (
                    MutableList<CategoryWorkoutsItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val categoryElem = mutableListOf<CategoryWorkoutsItem>()

                val categories = it.workoutCategoryDao().getAll()
                categories.map { category ->
                    val workouts: List<WorkoutEntity> = if (library) {
                        it.workoutDao().getByCategoryIdNotForAuthor(category.id, userId)
                    } else {
                        it.workoutDao().getByCategoryIdAndAuthorId(category.id, userId)

                    }
                    formList(workouts, category, categoryElem)
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(categoryElem)
                }
            }

        }
    }

    private fun formList(workouts: List<WorkoutEntity>, category: WorkoutCategoryEntity, categoryElem: MutableList<CategoryWorkoutsItem>): Boolean {
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
        return categoryElem.add(CategoryWorkoutsItem(category.id.toString(), category.name, workoutsList))
    }
}
