package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.AppDatabase
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem

abstract class UserFragment : BaseFragment() {
    protected fun loadSportsInfo(
            userId: Long,
            actionsAfter: (
                    List<WorkoutSportEntity>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userSports = it.userDao().getUserSports(userId)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(userSports)
                }
            }
        }
    }

    protected fun loadAuthorAndSportsInfo(
            userId: Long,
            actionsAfter: (
                    ProfileItem,
                    List<WorkoutSportEntity>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val authorEntity = it.userDao().getById(userId)
                val userSports = it.userDao().getUserSports(userId)
                val name = authorEntity.firstName + " ${authorEntity.fatherName}"
                val sportsList = formSportsList(it.userDao().getUserSports(authorEntity.id))
                val author = ProfileItem(
                        authorEntity.id.toString(),
                        authorEntity.id,
                        name,
                        sportsList,
                        false,
                        authorEntity.avatarUrl,
                        0,
                        0,
                        0,
                        0
                )
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(author, userSports)
                }
            }
        }
    }

    protected fun getAuthors(
            userId: Long,
            actionsAfter: (
                    MutableList<ProfileItem>?
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val authors = it.userDao().getAllExceptUser(userId)
                val authorsList = mutableListOf<ProfileItem>()
                for (author in authors) {
                    val name = author.firstName + " ${author.fatherName}"
                    val sportsList = formSportsList(it.userDao().getUserSports(author.id))
                    authorsList.add(ProfileItem(
                            author.id.toString(),
                            author.id,
                            name,
                            sportsList,
                            false,
                            author.avatarUrl,
                            0,
                            0,
                            0,
                            0
                    ))
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(authorsList)
                }
            }
        }
    }

    private fun formSportsList(list: List<WorkoutSportEntity>): MutableList<String> {
        val sportsList = mutableListOf<String>()
        for (sport in list) {
            sportsList.add(sport.name)
        }
        return sportsList
    }

    protected fun getUserWorkouts(
            userId: Long,
            actionsAfter: (
                    MutableList<ShortWorkoutItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userWorkouts = it.userDao().getUserWorkouts(userId)
                val workoutsList = formWorkouts(userWorkouts, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workoutsList)
                }
            }
        }
    }
    protected fun getUserWorkoutsPrivate(
            userId: Long,
            actionsAfter: (
                    MutableList<ShortWorkoutItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userWorkouts = it.userDao().getUserWorkoutsPublic(userId, false)
                val workoutsList = formWorkouts(userWorkouts, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workoutsList)
                }
            }
        }
    }

    protected fun getUserWorkoutsShared(
            userId: Long,
            actionsAfter: (
                    MutableList<ShortWorkoutItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userWorkouts = it.userDao().getUserWorkoutsPublic(userId, true)
                val workoutsList = formWorkouts(userWorkouts, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workoutsList)
                }
            }
        }
    }


    private fun formWorkouts(userWorkouts: List<WorkoutEntity>, it: AppDatabase): MutableList<ShortWorkoutItem> {
        return userWorkouts.map { workout ->
            val category = it.workoutCategoryDao().getById(workout.categoryId)
            val sport = it.workoutSportDao().getById(workout.sportId)
            val downloadsNumber = 0
            val rank = 0.0
            ShortWorkoutItem(
                    workout.id.toString(),
                    workout,
                    category.name,
                    sport.name,
                    downloadsNumber,
                    rank
            )
        }.toMutableList()
    }

    protected fun getUserExercises(
            userId: Long,
            actionsAfter: (
                    MutableList<ShortExerciseItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userExercises = it.userDao().getUserExercises(userId)
                val exercisesList = formExercises(userExercises, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(exercisesList)
                }
            }
        }
    }
    protected fun getUserExercisesPrivate(
            userId: Long,
            actionsAfter: (
                    MutableList<ShortExerciseItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userExercises = it.userDao().getUserExercisesPublic(userId, false)
                val exercisesList = formExercises(userExercises, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(exercisesList)
                }
            }
        }
    }
    protected fun getUserExercisesShared(
            userId: Long,
            actionsAfter: (
                    MutableList<ShortExerciseItem>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userExercises = it.userDao().getUserExercisesPublic(userId, true)
                val exercisesList = formExercises(userExercises, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(exercisesList)
                }
            }
        }
    }

    private fun formExercises(userExercises: List<ExerciseEntity>, it: AppDatabase): MutableList<ShortExerciseItem> {
        return userExercises.map { exercise ->
            val category = it.exerciseCategoryDao().getById(exercise.categoryId)
            val downloadsNumber = 0
            val rank = 0.0
            ShortExerciseItem(
                    exercise.id.toString(),
                    exercise,
                    category.name,
                    downloadsNumber,
                    rank
            )
        }.toMutableList()
    }

}