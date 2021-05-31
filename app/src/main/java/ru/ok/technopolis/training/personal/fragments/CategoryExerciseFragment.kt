package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.AppDatabase
import ru.ok.technopolis.training.personal.db.entity.ExerciseCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.items.CategoryExerciseItem
import ru.ok.technopolis.training.personal.items.ShortExerciseItem

import kotlin.random.Random

abstract class CategoryExerciseFragment : BaseFragment() {
    protected fun loadCategoryExercises(userId: Long, library: Boolean,
                                        actionsAfter: (
                                                MutableList<CategoryExerciseItem>
                                        ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val categoryElements = mutableListOf<CategoryExerciseItem>()
                val categories = it.exerciseCategoryDao().getAll()
                categories.forEach { category ->
                    val workouts: List<ExerciseEntity> = if (library) {
                        it.exerciseDao().getByCategoryIdNotForAuthor(category.id, userId)
                    } else {
                        it.exerciseDao().getByCategoryIdAndAuthorId(category.id, userId)

                    }
                    formList(workouts, category, categoryElements, it)
                }
                val filtered = categoryElements.filter { cat -> cat.exercises.isNotEmpty() }.toMutableList()
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(filtered)
                }
            }

        }
    }

    private fun formList(exercises: List<ExerciseEntity>, category: ExerciseCategoryEntity, categoryElem: MutableList<CategoryExerciseItem>, appDatabase: AppDatabase) {
        val exercisesList = exercises.map { entity ->
            val downloadsNumber = 0
            val rank = 0.0
            var image = appDatabase.exerciseMediaDao().getFirstByExerciseId(entity.id)
            if (image.isNullOrBlank()) {
                image = " "
            }
            ShortExerciseItem(
                    Random.nextInt().toString(),
                    entity,
                    category.name,
                    downloadsNumber,
                    rank,
                    image
            )
        }.toMutableList()
        categoryElem.add(CategoryExerciseItem(category.id.toString(), category.name, exercisesList))
    }
}