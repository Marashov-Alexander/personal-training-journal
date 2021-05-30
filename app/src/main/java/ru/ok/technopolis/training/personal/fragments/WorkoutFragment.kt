package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.items.ExerciseItem
import kotlin.random.Random

abstract class WorkoutFragment : BaseFragment() {
    protected fun loadWorkoutInfo(
        workoutId: Long,
        loadCategories: Boolean,
        loadSports: Boolean,
        actionsAfter: (
            WorkoutEntity,
            WorkoutCategoryEntity,
            WorkoutSportEntity,
            MutableList<ExerciseItem>,
            UserEntity?,
            List<WorkoutCategoryEntity>,
            List<WorkoutSportEntity>
        ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val workoutExercisesByWorkout = it.workoutExerciseDao().getAllByWorkout(workoutId)
                val workout = it.workoutDao().getById(workoutId)
                val author = it.userDao().getById(workout.authorId)
                val exercises = workoutExercisesByWorkout.map { workoutExercise ->
                    val exercise = it.exerciseDao().getById(workoutExercise.exerciseId)
                    val importantParameters = it.parameterDao().getImportantParameters(exercise.id)
                    val description =
                        if (importantParameters.isEmpty())
                            ""
                        else
                            importantParameters.map { info -> "${info.name}: ${info.value} ${info.units}" }.reduce { acc, str -> "$acc, $str" }
                    ExerciseItem(
                        Random.nextInt().toString(),
                        exercise,
                        workoutExercise,
                        description
                    )
                }.toMutableList()
                val category = it.workoutCategoryDao().getById(workout.categoryId)
                val sport = it.workoutSportDao().getById(workout.sportId)
                val categories = if (loadCategories) it.workoutCategoryDao().getAll() else listOf()
                val sports = if (loadSports) it.workoutSportDao().getAll() else listOf()
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workout, category, sport, exercises, author, categories, sports)
                }
            }
        }
    }
}
