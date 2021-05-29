package ru.ok.technopolis.training.personal.fragments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import kotlin.random.Random

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
                val name = authorEntity.firstName + "${authorEntity.fatherName}"
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
                    val name = author.firstName + "${author.fatherName}"
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
                val workoutsList = userWorkouts.map { workout ->
                    val category = it.workoutCategoryDao().getById(workout.categoryId)
                    val sport = it.workoutSportDao().getById(workout.sportId)
                    val downloadsNumber = 0
                    val rank = 0.0
                    ShortWorkoutItem(
                            workout.id.toString(),
                            workout.name,
                            category.name,
                            sport.name,
                            downloadsNumber,
                            rank
                    )
                }.toMutableList()
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workoutsList)
                }
            }
        }
    }

    protected fun getUserExercises(
            userId: Long,
            actionsAfter: (
                    List<ExerciseEntity>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val userExercises = it.userDao().getUserExercises(userId)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(userExercises)
                }
            }
        }
    }

}