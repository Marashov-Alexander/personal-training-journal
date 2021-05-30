package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.entity.ExerciseCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.items.CategoryExerciseItem
import ru.ok.technopolis.training.personal.items.ShortExerciseItem

import kotlin.random.Random

abstract class CategoryExerciseFragment : BaseFragment() {
    protected fun loadCategoryExercises( userId: Long, library: Boolean,
                                        actionsAfter: (
                                                MutableList<CategoryExerciseItem>
                                        ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val categoryElem = mutableListOf<CategoryExerciseItem>()
                val categories = it.exerciseCategoryDao().getAll()
                categories.map { category ->
                    val workouts: List<ExerciseEntity> = if (library) {
                        it.exerciseDao().getByCategoryIdNotForAuthor(category.id, userId)
                    } else {
                        it.exerciseDao().getByCategoryIdAndAuthorId(category.id, userId)

                    }
                    formList(workouts, category, categoryElem)
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(categoryElem)
                }
            }

        }
    }

    private fun formList(exercises: List<ExerciseEntity>, category: ExerciseCategoryEntity, categoryElem: MutableList<CategoryExerciseItem>): Boolean {
        val exercisesList = exercises.map { entity ->
            val downloadsNumber = 0
            val rank = 0.0
            ShortExerciseItem(
                    Random.nextInt().toString(),
                    entity,
                    category.name,
                    downloadsNumber,
                    rank
            )
        }.toMutableList()
        return categoryElem.add(CategoryExerciseItem(category.id.toString(), category.name, exercisesList))
    }
}