package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.AppDatabase
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import kotlin.random.Random

abstract class CategoryWorkoutFragment : WorkoutFragment() {
    protected fun loadCategoryWorkouts( userId: Long, library: Boolean,
            actionsAfter: (
                    MutableList<CategoryWorkoutsItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val categoryElements = mutableListOf<CategoryWorkoutsItem>()

                val categories = it.workoutCategoryDao().getAll()
                categories.forEach { category ->
                    val workouts: List<WorkoutEntity> = if (library) {
                        it.workoutDao().getByCategoryIdNotForAuthor(category.id, userId)
                    } else {
                        it.workoutDao().getByCategoryIdAndAuthorId(category.id, userId)
                    }
                    formList(workouts, category, it, categoryElements)
                }
                val filtered = categoryElements.filter { cat -> cat.worlouts.isNotEmpty() }.toMutableList()
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(filtered)
                }
            }

        }
    }

    private fun formList(workouts: List<WorkoutEntity>, category: WorkoutCategoryEntity, db: AppDatabase, categoryElem: MutableList<CategoryWorkoutsItem>) {
        val workoutsList = workouts.map { workout ->
            val sport = db.workoutSportDao().getById(workout.sportId)
            var image = db.workoutMediaDao().getByFirstWorkoutId(workout.id)
            if (image.isNullOrBlank()) {
                image = " "
            }
            val downloadsNumber = 0
            val rank = 0.0
            ShortWorkoutItem(
                    Random.nextInt().toString(),
                    workout,
                    category.name,
                    sport.name,
                    downloadsNumber,
                    rank,
                    image
            )
        }.toMutableList()
        categoryElem.add(CategoryWorkoutsItem(category.id.toString(), category.name, workoutsList))
    }
}
