package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.ExerciseItem
import kotlin.random.Random

abstract class WorkoutFragment : BaseFragment() {
    protected fun loadWorkoutInfo(workoutId: Long, actionsAfter: (WorkoutEntity, WorkoutCategoryEntity, MutableList<ExerciseItem>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val workoutExercisesByWorkout = it.workoutExerciseDao().getAllByWorkout(workoutId)
                val workout = it.workoutDao().getById(workoutId)
                val exercises = workoutExercisesByWorkout.map { workoutExercise ->
                    ExerciseItem(
                        Random.nextInt().toString(),
                        it.exerciseDao().getById(workoutExercise.exerciseId),
                        workoutExercise
                    )
                }.toMutableList()
                val category = it.workoutCategoryDao().getById(1)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workout, category, exercises)
                }
            }
        }
    }
}
