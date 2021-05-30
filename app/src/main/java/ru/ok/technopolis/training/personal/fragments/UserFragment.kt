package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.AppDatabase
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.items.ChatItem
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
                val author = formProfiles(listOf(authorEntity), it).single()
                val userSports = it.userDao().getUserSports(userId)
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
                val authorsList = formProfiles(authors, it)
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

    protected fun getUserSubscribers (userId: Long,
    actionsAfter: (
    MutableList<ProfileItem>
    ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val allSubscribers = it.userDao().getAllUSerSubscribers(userId)
                val subscribers = formProfiles(allSubscribers, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(subscribers)
                }
            }
        }
    }

    protected fun getUserSubscriptions (userId: Long,
                                      actionsAfter: (
                                              MutableList<ProfileItem>
                                      ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val allSubscribers = it.userDao().getAllUSerSubscriptions(userId)
                val subscribers = formProfiles(allSubscribers, it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(subscribers)
                }
            }
        }
    }

    protected fun getUser(userId: Long,
                           actionsAfter: (ProfileItem) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val user = it.userDao().getById(userId)
                val subscribers = formProfiles(listOf(user), it)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(subscribers.single())
                }
            }
        }
    }


    private fun formProfiles(elements: List<UserEntity>, db: AppDatabase): MutableList<ProfileItem> {
        val authorsList = mutableListOf<ProfileItem>()
        for (element in elements) {
            val name = element.firstName + " ${element.fatherName}"
            val sportsList = formSportsList(db.userDao().getUserSports(element.id))
            val subscribers= db.userDao().getAllUSerSubscribers(element.id).size
            val subscriptions = db.userDao().getAllUSerSubscriptions(element.id).size
            val sharedWorkouts = db.userDao().getUserWorkoutsPublic(element.id, true).size
            val sharedExercises = db.userDao().getUserExercisesPublic(element.id, true).size
            authorsList.add(ProfileItem(
                    element.id.toString(),
                    element.id,
                    name,
                    sportsList,
                    false,
                    element.avatarUrl,
                    subscribers,
                    subscriptions,
                    sharedWorkouts,
                    sharedExercises
            ))
        }
        return authorsList
    }

}