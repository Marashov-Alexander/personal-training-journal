package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.entity.*
import ru.ok.technopolis.training.personal.items.ParameterItem
import kotlin.random.Random

abstract class ExerciseFragment : BaseFragment() {

    protected fun loadExerciseInfo(
        userId: Long,
        workoutId: Long?,
        exerciseId: Long,
        actionsAfter: (
            exercise: ExerciseEntity,
            author: UserEntity,
            userLevel: UserLevelEntity?,
            levelsMap: MutableMap<Int, MutableList<ParameterItem>>,
            maxLevel: Int,
            mediaList: List<ExerciseMediaEntity>
        ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            var maxLevel = 0
            database!!.let {
                val exercise = it.exerciseDao().getById(exerciseId)
                val mediaList = it.exerciseMediaDao().getByExerciseId(exerciseId)
                val author = it.userDao().getById(exercise.authorId)
                val exerciseParameters = it.exerciseParameterDao().getAllByExercise(exerciseId)
                val parameters = it.exerciseParameterDao().getParametersForExercise(exerciseId)
                val levelsMap = mutableMapOf<Int, MutableList<ParameterItem>>()
                parameters.forEach { param ->
                    val exerciseParameter = exerciseParameters.find { exerciseParam -> exerciseParam.parameterId == param.id }!!
                    val levels = it.levelExerciseParameterDao().getAllByExerciseParameterId(exerciseParameter.id)

                    for (levelExerciseParam in levels) {
                        val parameterItems = levelsMap.getOrPut(levelExerciseParam.level, { mutableListOf() })
                        parameterItems.add(ParameterItem(
                            Random.nextInt().toString(),
                            param,
                            levelExerciseParam,
                            editable = true
                        ))
                        maxLevel = Integer.max(maxLevel, levelExerciseParam.level)
                    }
                }
                var userLevel: UserLevelEntity? = null
                workoutId?.let { workoutId ->
                    val workoutExercise = it.workoutExerciseDao().getById(workoutId, exerciseId)
                    userLevel = it.userLevelDao().getById(userId, workoutExercise.id)
                    if (userLevel == null) {
                        userLevel = UserLevelEntity(userId, workoutExercise.id, 1)
                        it.userLevelDao().insert(userLevel!!)
                    }
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(exercise, author, userLevel, levelsMap, maxLevel, mediaList)
                }
            }

        }
    }

    protected fun saveChanges(
        // объект упражнения с обновлёнными полями
        exercise: ExerciseEntity,
        levelsCount: Int,
        levelsMap: MutableMap<Int, MutableList<ParameterItem>>,
        removedParameters: MutableSet<Long>,
        actionsAfter: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            database?.let { database ->
                for (entry in levelsMap.entries) {
                    val parameters = entry.value
                    for (parameter in parameters) {
                        val parameterEntity = parameter.parameter
                        val levelEntity = parameter.levelExerciseParameterEntity
                        if (parameterEntity.id == 0L) {
                            parameterEntity.id = database.parameterDao().insert(parameterEntity)
                        } else {
                            database.parameterDao().update(parameterEntity)
                        }
                        val exerciseParametersList = database.exerciseParameterDao().getByExerciseAndParameter(exercise.id, parameterEntity.id)
                        var exerciseParameterEntity = exerciseParametersList.getOrNull(0)
                        if (exerciseParameterEntity == null) {
                            exerciseParameterEntity = ExerciseParameterEntity(exercise.id, parameterEntity.id)
                            exerciseParameterEntity.id = database.exerciseParameterDao().insert(exerciseParameterEntity)
                        }
                        levelEntity.exerciseParameterId = exerciseParameterEntity.id

                        val levelExerciseParameterEntityList = database.levelExerciseParameterDao().getById(levelEntity.level, levelEntity.exerciseParameterId)
                        val levelExerciseParameterEntity = levelExerciseParameterEntityList.getOrNull(0)
                        if (levelExerciseParameterEntity == null) {
                            database.levelExerciseParameterDao().insert(levelEntity)
                        } else {
                            database.levelExerciseParameterDao().update(levelEntity)
                        }
                    }
                }
                // очистка удалённых уровней
                database.levelExerciseParameterDao().deleteLevelsGreaterThan(levelsCount, exercise.id)
                // очистка удалённых параметров
                for (removedParameterId in removedParameters) {
                    database.exerciseParameterDao().delete(exercise.id, removedParameterId)
                }
                database.exerciseDao().update(exercise)
            }
            withContext(Dispatchers.Main) {
                removedParameters.clear()
                actionsAfter.invoke()
            }
        }
    }

}