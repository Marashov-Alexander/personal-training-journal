package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Single
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.api.Api
import ru.ok.technopolis.training.personal.db.entity.DoneExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseTypeEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterResultEntity
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WithServerId
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.dto.db.DoneExerciseDto
import ru.ok.technopolis.training.personal.dto.db.ExerciseDto
import ru.ok.technopolis.training.personal.dto.db.ExerciseParameterDto
import ru.ok.technopolis.training.personal.dto.db.ExerciseTypeDto
import ru.ok.technopolis.training.personal.dto.db.ParameterDto
import ru.ok.technopolis.training.personal.dto.db.ResultDto
import ru.ok.technopolis.training.personal.dto.db.UserWorkoutDto
import ru.ok.technopolis.training.personal.dto.db.WorkoutDto
import ru.ok.technopolis.training.personal.dto.db.WorkoutExerciseDto
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.logger.Logger
import java.net.HttpURLConnection
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KFunction

class UploadFragment : BaseFragment() {

    private var progressBar: ProgressBar? = null
    private var status: TextView? = null
    private var savedCount = AtomicInteger(0)
    private var failedCount = AtomicInteger(0)
    private val uploadScope = CoroutineScope(Dispatchers.IO)

    private var savesFun: Array<() -> Unit> = arrayOf({}, {}, {}, {}, {}, {}, {}, {}, {})
    private var index = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = uploading_progress_bar
        status = status_text

        uploadScope.launch(Dispatchers.IO) {
            database?.let { appDatabase ->
                val email = CurrentUserRepository.currentUser.value?.email
                val user = appDatabase.userDao().getByEmail(email!!)
                val userWorkoutList = appDatabase.userWorkoutDao().getAll()
                val userWorkoutForUserList = userWorkoutList.filter { it.userId == user.id }
                val doneExerciseList = appDatabase.doneExerciseDao().getAllByUser(user.id)
                val parametersResultList = doneExerciseList.flatMap {
                    appDatabase.parameterResultDao().getAllByDoneExerciseId(it.id)
                }
                val workoutsForUser = appDatabase.userWorkoutDao().getWorkoutsForUser(user.id)
                val workoutExerciseList = workoutsForUser.flatMap {
                    appDatabase.workoutExerciseDao().getAllByWorkout(it.id)
                }.distinct()
                val exercisesForUser = workoutsForUser.flatMap {
                    appDatabase.workoutExerciseDao().getExercisesForWorkout(it.id)
                }.distinct()
                val parametersForUser = exercisesForUser.flatMap {
                    appDatabase.exerciseParameterDao().getParametersForExercise(it.id)
                }.distinct()
                val exerciseParameterList = exercisesForUser.flatMap {
                    appDatabase.exerciseParameterDao().getAllByExercise(it.id)
                }.distinct()
                val exerciseTypeList = appDatabase.exerciseTypeDao().getAll()

                val unsavedExerciseTypeList = exerciseTypeList.filter { it.serverId == -1L }
                val unsavedParameters = parametersForUser.filter { it.serverId == -1L }
                val unsavedExercises = exercisesForUser.filter { it.serverId == -1L }
                val unsavedExerciseParameterList = exerciseParameterList.filter { it.serverId == -1L }
                val unsavedWorkoutsForUser = workoutsForUser.filter { it.serverId == -1L }
                val unsavedWorkoutExerciseList = workoutExerciseList.filter { it.serverId == -1L }
                val unsavedUserWorkoutForUserList = userWorkoutForUserList.filter { it.serverId == -1L }
                val unsavedDoneExerciseList = doneExerciseList.filter { it.serverId == -1L }
                val unsavedResultsList = parametersResultList.filter { it.serverId == -1L }

                val deletedUserWorkoutForUserList = userWorkoutForUserList.filter { it.deleted }
                val deletedWorkoutExerciseList = workoutExerciseList.filter { it.deleted }
                val deletedExerciseParameterList = exerciseParameterList.filter { it.deleted }

                val unsavedCount = unsavedResultsList.size + unsavedDoneExerciseList.size + unsavedUserWorkoutForUserList.size + unsavedWorkoutsForUser.size + unsavedWorkoutExerciseList.size + unsavedExercises.size + unsavedParameters.size + unsavedExerciseParameterList.size + unsavedExerciseTypeList.size
                val deletedCount = deletedUserWorkoutForUserList.size + deletedWorkoutExerciseList.size + deletedExerciseParameterList.size

                val count = unsavedCount + deletedCount
                withContext(Dispatchers.Main) {
                    progressBar?.max = count
                    progressBar?.progress = 0
                    status?.text = count.toString()
                }

                val token = CurrentUserRepository.getCurrentUserToken(requireContext())!!

                var i = 0
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.exerciseTypeDao().update(data as List<ExerciseTypeEntity>) },
                        unsavedExerciseTypeList, unsavedExerciseTypeList.map { ExerciseTypeDto(it.name) },
                        Api::createExerciseType
                    )
                }
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.parameterDao().update(data as List<ParameterEntity>) },
                        unsavedParameters, unsavedParameters.map { ParameterDto(it.name, it.measureUnit, it.resultType, it.value) },
                        Api::createParameter
                    )
                }
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.exerciseDao().update(data as List<ExerciseEntity>) },
                        unsavedExercises, unsavedExercises.map { ExerciseDto(it.name, appDatabase.exerciseTypeDao().getById(it.typeId).serverId) },
                        Api::createExercise
                    )
                }
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.exerciseParameterDao().update(data as List<ExerciseParameterEntity>) },
                        unsavedExerciseParameterList, unsavedExerciseParameterList.map {
                            ExerciseParameterDto(
                                appDatabase.exerciseDao().getById(it.exerciseId).serverId,
                                appDatabase.parameterDao().getById(it.parameterId).serverId
                            )
                        },
                        Api::createExerciseParameter
                    )
                }
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.workoutDao().update(data as List<WorkoutEntity>) },
                        unsavedWorkoutsForUser, unsavedWorkoutsForUser.map { WorkoutDto(it.name, it.plannedTime, it.weekdaysMask) },
                        Api::createWorkout
                    )
                }
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.workoutExerciseDao().update(data as List<WorkoutExerciseEntity>) },
                        unsavedWorkoutExerciseList, unsavedWorkoutExerciseList.map {
                            WorkoutExerciseDto(
                                appDatabase.workoutDao().getById(it.workoutId).serverId,
                                appDatabase.exerciseDao().getById(it.exerciseId).serverId
                            )
                        },
                        Api::createWorkoutExercise
                    )
                }
                println(unsavedUserWorkoutForUserList)
                println(unsavedUserWorkoutForUserList.map {
                    UserWorkoutDto(
                        appDatabase.workoutDao().getById(it.workoutId).serverId
                    )})
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.userWorkoutDao().update(data as List<UserWorkoutEntity>) },
                        unsavedUserWorkoutForUserList, unsavedUserWorkoutForUserList.map {
                        UserWorkoutDto(
                            appDatabase.workoutDao().getById(it.workoutId).serverId
                        )
                    },
                        Api::createUserWorkout
                    )
                }
                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.doneExerciseDao().update(data as List<DoneExerciseEntity>) },
                        unsavedDoneExerciseList, unsavedDoneExerciseList.map {
                        DoneExerciseDto(
                            appDatabase.exerciseDao().getById(it.exerciseId).serverId,
                            "2020-05-28"
                        )
                    },
                        Api::createDoneExercise
                    )
                }

                savesFun[i++] = {
                    saveData(
                        token,
                        { data -> appDatabase.parameterResultDao().update(data as List<ParameterResultEntity>) },
                        unsavedResultsList, unsavedResultsList.map {
                        ResultDto(
                            appDatabase.doneExerciseDao().getById(it.doneExerciseId).serverId,
                            appDatabase.parameterDao().getById(it.parameterId).serverId,
                            it.value
                        )
                    },
                        Api::createResult
                    )
                }

                saveNext()

            }
        }
    }

    private fun saveNext() {
        ++index
        if (index < savesFun.size) {
            savesFun[index].invoke()
        } else {
            uploadScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "Done!", Toast.LENGTH_LONG).show()
                router?.goToPrevFragment()
            }
        }
    }

    private fun saveData(
        token: String,
        savingFunction: (data: List<WithServerId>) -> Unit,
        data: List<WithServerId>,
        savingData: List<*>, method: KFunction<*>
    ) {
        if (savingData.isNotEmpty()) {
            taskContainer.add((method.call(
                savingData, token
            ) as Single<Response<List<Long>>>).subscribe(
                {
                    onSuccessSave(it)
                    val ids = it.body()!!
                    for (i in ids.indices) {
                        println("before ${data[i]}")
                        data[i].serverId(ids[i])
                        println("after ${data[i]}")
                    }
                    uploadScope.launch(Dispatchers.IO) {
                        savingFunction.invoke(data)
                        withContext(Dispatchers.Main) {
                            savedCount.addAndGet(data.size)
                            progressBar?.progress = savedCount.get()
                        }
                        saveNext()
                    }
                },
                { thr ->
                    uploadScope.launch(Dispatchers.Main) {
                        onFailureSave(thr)
                        failedCount.incrementAndGet()
                    }
                }
            ))
        } else {
            onSuccessSave()
            saveNext()
        }
    }

    private fun onSuccessSave(it: Response<*>? = null) {
        if (it == null) {
            Logger.d(this, "Nothing to save")
        } else {
            when {
                it.code() == HttpURLConnection.HTTP_OK -> {
                    Logger.d(this, "successfully request with code ${it.code()}")
                }
                it.code() == HttpURLConnection.HTTP_CREATED -> {
                    Logger.d(this, "successfully created with code ${it.code()}")
                }
                it.code() == HttpURLConnection.HTTP_NO_CONTENT -> {
                    Logger.d(this, "successfully deleted with code ${it.code()}")
                }
            }
        }
    }

    private fun onFailureSave(t: Throwable) {
        val str = "Data saving failed: ${t.localizedMessage}"
        Logger.e(this, t.stackTrace)
        status?.text = str
        failedCount.incrementAndGet()
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_upload
}
