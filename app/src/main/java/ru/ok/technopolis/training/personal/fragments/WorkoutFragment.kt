package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.entity.*
import ru.ok.technopolis.training.personal.items.ExerciseItem
import kotlin.random.Random

abstract class WorkoutFragment : BaseFragment() {
    protected fun loadWorkoutInfo(
            userId: Long?,
            workoutId: Long,
            loadCategories: Boolean,
            loadSports: Boolean,
            actionsAfter: (
                    WorkoutEntity,
                    UserWorkoutEntity?,
                    WorkoutCategoryEntity,
                    WorkoutSportEntity,
                    MutableList<ExerciseItem>,
                    UserEntity?,
                    List<WorkoutCategoryEntity>,
                    List<WorkoutSportEntity>,
                    List<WorkoutMediaEntity>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val workoutExercisesByWorkout = it.workoutExerciseDao().getAllByWorkout(workoutId)
                val workout = it.workoutDao().getById(workoutId)
                val mediaData = it.workoutMediaDao().getByWorkoutId(workoutId)
                val author = it.userDao().getById(workout.authorId)
                val userWorkoutEntity = if (userId == null) null else it.userWorkoutDao().getById(userId, workout.id)
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
                    actionsAfter.invoke(workout, userWorkoutEntity, category, sport, exercises, author, categories, sports, mediaData)
                }
            }
        }
    }

    protected fun createExercisesList(workoutId: Long, actionsAfter: (LongArray, IntArray, IntArray) -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val allByWorkout = it.workoutExerciseDao().getAllByWorkout(workoutId)
                val flatExercisesWex = mutableListOf<Long>()
                val flatExercisesCnt = mutableListOf<Int>()
                val groupBy = allByWorkout.groupBy { exWrk -> exWrk.supersetGroupId }
                groupBy.forEach { entry ->
                    val lst = entry.value
                    val counter = lst.first().counter ?: 1
                    for (currentCounter in 1..counter) {
                        lst.forEach { wex ->
                            flatExercisesWex.add(wex.id)
                            flatExercisesCnt.add(currentCounter)
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(
                            flatExercisesWex.toLongArray(),
                            flatExercisesCnt.toIntArray(),
                            IntArray(flatExercisesWex.size) {0}
                    )
                }
            }
        }
    }

    protected fun createNewExercise(userId: Long, workoutId: Long?, orderNumber: Int, actionsAfter: (Long) -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val newExercise = ExerciseEntity("", "", "Не указано",1, false, userId)
                newExercise.id = it.exerciseDao().insert(newExercise)

                val nevExerciseRest = ExerciseParameterEntity(newExercise.id, 1L)
                nevExerciseRest.id = it.exerciseParameterDao().insert(nevExerciseRest)
                val rstLevel = LevelExerciseParameterEntity(1, 15f, nevExerciseRest.id)
                it.levelExerciseParameterDao().insert(rstLevel)

                val nevExerciseRepeats = ExerciseParameterEntity(newExercise.id, 2L)
                nevExerciseRepeats.id = it.exerciseParameterDao().insert(nevExerciseRepeats)
                val rptLevel = LevelExerciseParameterEntity(1, 1f, nevExerciseRepeats.id)
                it.levelExerciseParameterDao().insert(rptLevel)

                if (workoutId != null) {
                    val newWorkoutExercise = WorkoutExerciseEntity(workoutId, newExercise.id, orderNumber)
                    newWorkoutExercise.id = it.workoutExerciseDao().insert(newWorkoutExercise)
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(newExercise.id)
                }
            }
        }
    }

    protected fun createNewWorkout(userId: Long, actionsAfter: (Long) -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val newWorkout = WorkoutEntity("", "", 1L, 1L, 1, false, userId)
                newWorkout.id = it.workoutDao().insert(newWorkout)
                val newUserWorkout = UserWorkoutEntity(userId, newWorkout.id, true, System.currentTimeMillis())
                newUserWorkout.id = it.userWorkoutDao().insert(newUserWorkout)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(newWorkout.id)
                }
            }
        }
    }
}
