package ru.ok.technopolis.training.personal.api

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.ok.technopolis.training.personal.dto.UserDto
import ru.ok.technopolis.training.personal.dto.UserSignUpDto
import ru.ok.technopolis.training.personal.dto.db.DoneExerciseDto
import ru.ok.technopolis.training.personal.dto.db.ExerciseDto
import ru.ok.technopolis.training.personal.dto.db.ExerciseParameterDto
import ru.ok.technopolis.training.personal.dto.db.ExerciseTypeDto
import ru.ok.technopolis.training.personal.dto.db.MeasureUnitDto
import ru.ok.technopolis.training.personal.dto.db.ParameterDto
import ru.ok.technopolis.training.personal.dto.db.ResultDto
import ru.ok.technopolis.training.personal.dto.db.UserWorkoutDto
import ru.ok.technopolis.training.personal.dto.db.WorkoutDto
import ru.ok.technopolis.training.personal.dto.db.WorkoutExerciseDto
import java.util.*

/**
 *
 * High-level access to api
 *
 */
object Api {

    private val api = RetrofitApiUtils.createApi()

    /**
     *
     * Example:
     * User: class, required in our client
     * UserDto: class, that represents entity, returned from request
     *
     * Reason: sometimes it's easier to use data structure different from
     * structure retrieved from backend
     *
     * So, it's better to have two separate classes, one for data transfer
     * and one for usage in our app
     *
     * Also, other actions could be performed in these methods, including
     * subscribeOn() and observeOn()
     *
     *  fun getUserTrainings(user: User) =
     *      api.getUserTrainings(user.uid)
     *          .subscribeOn(Schedulers.io())
     *          .observeOn(AndroidSchedulers.mainThread())
     *          .map {
     *              Training.fromDto(it)
     *          }
     *          .some_other_logic
     */

    fun createUser(userSignUpDto: UserSignUpDto): Single<Response<UserDto>> =
        api.createUserRequest(userSignUpDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun login(token: String): Single<Response<UserDto>> =
        api.loginRequest(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun fakeLogin(token: String): Single<Response<UserDto>> {
        val testUser = UserDto(
                1,
                "uuid",
                "Testov",
                "Tester",
                "Testerovich",
                "test@test.test",
                Date(System.currentTimeMillis()),
                "Male",
                null
        )
        val response = Response.success(200, testUser)
        return Single.just(response)
    }

    fun changeUserData(id: Long, nameDto: Any, token: String): Single<Response<UserDto>> =
        api.changeUserData(id, token, nameDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createDoneExercise(doneExerciseDto: List<DoneExerciseDto>, token: String): Single<Response<List<Long>>> =
        api.createDoneExercise(token, doneExerciseDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createExercise(exerciseDto: List<ExerciseDto>, token: String): Single<Response<List<Long>>> =
        api.createExercise(token, exerciseDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createExerciseParameter(exerciseParameterDto: List<ExerciseParameterDto>, token: String): Single<Response<List<Long>>> =
        api.createExerciseParameter(token, exerciseParameterDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createExerciseType(exerciseTypeDto: List<ExerciseTypeDto>, token: String): Single<Response<List<Long>>> =
        api.createExerciseTypeRequest(token, exerciseTypeDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createParameter(parameterDto: List<ParameterDto>, token: String): Single<Response<List<Long>>> =
        api.createParameter(token, parameterDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createResult(resultDto: List<ResultDto>, token: String): Single<Response<List<Long>>> =
        api.createResult(token, resultDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createWorkout(workout: List<WorkoutDto>, token: String): Single<Response<List<Long>>> =
        api.createWorkout(token, workout)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createWorkoutExercise(workoutExerciseDto: List<WorkoutExerciseDto>, token: String): Single<Response<List<Long>>> =
        api.createWorkoutExercise(token, workoutExerciseDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createUserWorkout(userWorkoutDto: List<UserWorkoutDto>, token: String): Single<Response<List<Long>>> =
        api.createUserWorkout(token, userWorkoutDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateDoneExercise(id: Long, doneExerciseDto: DoneExerciseDto, token: String): Single<Response<DoneExerciseDto>> =
        api.updateDoneExercise(id, token, doneExerciseDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateExercise(id: Long, exerciseDto: ExerciseDto, token: String): Single<Response<ExerciseDto>> =
        api.updateExercise(id, token, exerciseDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateExerciseParameter(id: Long, exerciseParameterDto: ExerciseParameterDto, token: String): Single<Response<ExerciseParameterDto>> =
        api.updateExerciseParameter(id, token, exerciseParameterDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateExerciseType(id: Long, exerciseTypeDto: ExerciseTypeDto, token: String): Single<Response<ExerciseTypeDto>> =
        api.updateExerciseTypeRequest(id, token, exerciseTypeDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateMeasureUnit(id: Long, measureUnitDto: MeasureUnitDto, token: String): Single<Response<MeasureUnitDto>> =
        api.updateMeasureUnitRequest(id, token, measureUnitDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateParameter(id: Long, parameterDto: ParameterDto, token: String): Single<Response<ParameterDto>> =
        api.updateParameter(id, token, parameterDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateResult(id: Long, resultDto: ResultDto, token: String): Single<Response<ResultDto>> =
        api.updateResult(id, token, resultDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateWorkout(id: Long, workout: WorkoutDto, token: String): Single<Response<WorkoutDto>> =
        api.updateWorkout(id, token, workout)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateWorkoutExercise(id: Long, workoutExerciseDto: WorkoutExerciseDto, token: String): Single<Response<WorkoutExerciseDto>> =
        api.updateWorkoutExercise(id, token, workoutExerciseDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateUserWorkout(id: Long, userWorkoutDto: UserWorkoutDto, token: String): Single<Response<UserWorkoutDto>> =
        api.updateUserWorkout(id, token, userWorkoutDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun deleteUserExerciseParameter(id: Long, token: String): Single<Response<Any>> =
        api.deleteUserExerciseParameterRequest(token, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun deleteUserWorkout(id: Long, token: String): Single<Response<Any>> =
        api.deleteUserWorkoutRequest(token, id)
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

    fun deleteWorkoutExercise(id: Long, token: String): Single<Response<Any>> =
        api.deleteWorkoutExerciseRequest(token, id)
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())



    fun getDoneExercise(token: String): Single<Response<List<DoneExerciseDto>>> =
        api.getDoneExercise(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getExercise(token: String): Single<Response<List<ExerciseDto>>> =
        api.getExercise(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getExerciseParameter(token: String): Single<Response<List<ExerciseParameterDto>>> =
        api.getExerciseParameter(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getExerciseType(token: String): Single<Response<List<ExerciseTypeDto>>> =
        api.getExerciseTypeRequest(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getParameter(token: String): Single<Response<List<ParameterDto>>> =
        api.getParameter(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getResult(token: String): Single<Response<List<ResultDto>>> =
        api.getResult(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getWorkout(token: String): Single<Response<List<WorkoutDto>>> =
        api.getWorkout(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getWorkoutExercise(token: String): Single<Response<List<WorkoutExerciseDto>>> =
        api.getWorkoutExercise(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getUserWorkout(token: String): Single<Response<List<UserWorkoutDto>>> =
        api.getUserWorkout(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}