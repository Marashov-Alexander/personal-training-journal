package ru.ok.technopolis.training.personal.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.ok.technopolis.training.personal.db.dao.ParameterResultDao
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

/**
 * Your Request-methods should be here
 */
interface ApiInterface {

    /**
     * @GET("path/path...")
     * fun getUserTrainings(userId: String): Single<TrainingDto>
     */

    @Headers("Content-Type: application/json")
    @POST("/users/create")
    fun createUserRequest(@Body userSignUpDto: UserSignUpDto): Single<Response<UserDto>>

    @GET("/users/login")
    fun loginRequest(@Header("Authorization") token: String): Single<Response<UserDto>>

    @PATCH("/users/{id}")
    fun changeUserData(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body nameDto: Any
    ): Single<Response<UserDto>>

    @Headers("Content-Type: application/json")
    @POST("/exercise-types/create")
    fun createExerciseTypeRequest(
        @Header("Authorization") token: String,
        @Body exerciseTypeDto: List<ExerciseTypeDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/measure-units/create")
    fun createMeasureUnitRequest(
        @Header("Authorization") token: String,
        @Body measureUnitDto: List<MeasureUnitDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/exercise-parameters/create")
    fun createExerciseParameter(
        @Header("Authorization") token: String,
        @Body exerciseParameterDto: List<ExerciseParameterDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/parameters/create")
    fun createParameter(
        @Header("Authorization") token: String,
        @Body parameterDto: List<ParameterDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/exercises/create")
    fun createExercise(
        @Header("Authorization") token: String,
        @Body exerciseDto: List<ExerciseDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/workout-exercises/create")
    fun createWorkoutExercise(
        @Header("Authorization") token: String,
        @Body workoutExerciseDto: List<WorkoutExerciseDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/workouts/create")
    fun createWorkout(
        @Header("Authorization") token: String,
        @Body workoutDto: List<WorkoutDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/user-workouts/create")
    fun createUserWorkout(
        @Header("Authorization") token: String,
        @Body userWorkoutDto: List<UserWorkoutDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/done-exercises/create")
    fun createDoneExercise(
        @Header("Authorization") token: String,
        @Body doneExerciseDto: List<DoneExerciseDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @POST("/parameter-results/create")
    fun createResult(
        @Header("Authorization") token: String,
        @Body resultDto: List<ResultDto>
    ): Single<Response<List<Long>>>

    @Headers("Content-Type: application/json")
    @PATCH("/exercise-types/{id}")
    fun updateExerciseTypeRequest(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body exerciseTypeDto: ExerciseTypeDto
    ): Single<Response<ExerciseTypeDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/measure-units/{id}")
    fun updateMeasureUnitRequest(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body measureUnitDto: MeasureUnitDto
    ): Single<Response<MeasureUnitDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/exercise-parameters/{id}")
    fun updateExerciseParameter(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body exerciseParameterDto: ExerciseParameterDto
    ): Single<Response<ExerciseParameterDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/parameters/{id}")
    fun updateParameter(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body parameterDto: ParameterDto
    ): Single<Response<ParameterDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/exercises/{id}")
    fun updateExercise(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body exerciseDto: ExerciseDto
    ): Single<Response<ExerciseDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/workout-exercises/{id}")
    fun updateWorkoutExercise(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body workoutExerciseDto: WorkoutExerciseDto
    ): Single<Response<WorkoutExerciseDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/workouts/{id}")
    fun updateWorkout(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body workoutDto: WorkoutDto
    ): Single<Response<WorkoutDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/user-workouts/{id}")
    fun updateUserWorkout(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body userWorkoutDto: UserWorkoutDto
    ): Single<Response<UserWorkoutDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/done-exercises/{id}")
    fun updateDoneExercise(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body doneExerciseDto: DoneExerciseDto
    ): Single<Response<DoneExerciseDto>>

    @Headers("Content-Type: application/json")
    @PATCH("/results/{id}")
    fun updateResult(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body resultDto: ResultDto
    ): Single<Response<ResultDto>>

    @DELETE("/user-workouts/{id}")
    fun deleteUserWorkoutRequest(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Single<Response<Any>>

    @DELETE("/user-workouts/{id}")
    fun deleteWorkoutExerciseRequest(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Single<Response<Any>>

    @DELETE("/user-workouts/{id}")
    fun deleteUserExerciseParameterRequest(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Single<Response<Any>>



    @GET("/exercise-types/all")
    fun getExerciseTypeRequest(
        @Header("Authorization") token: String
    ): Single<Response<List<ExerciseTypeDto>>>

    @GET("/measure-units/all")
    fun getMeasureUnitRequest(
        @Header("Authorization") token: String
    ): Single<Response<List<MeasureUnitDto>>>


    @GET("/exercise-parameters/all")
    fun getExerciseParameter(
        @Header("Authorization") token: String
    ): Single<Response<List<ExerciseParameterDto>>>


    @GET("/parameters/all")
    fun getParameter(
        @Header("Authorization") token: String
    ): Single<Response<List<ParameterDto>>>


    @GET("/exercises/all")
    fun getExercise(
        @Header("Authorization") token: String
    ): Single<Response<List<ExerciseDto>>>


    @GET("/workout-exercises/all")
    fun getWorkoutExercise(
        @Header("Authorization") token: String
    ): Single<Response<List<WorkoutExerciseDto>>>


    @GET("/workouts/all")
    fun getWorkout(
        @Header("Authorization") token: String
    ): Single<Response<List<WorkoutDto>>>


    @GET("/user-workouts/all")
    fun getUserWorkout(
        @Header("Authorization") token: String
    ): Single<Response<List<UserWorkoutDto>>>


    @GET("/done-exercises/all")
    fun getDoneExercise(
        @Header("Authorization") token: String
    ): Single<Response<List<DoneExerciseDto>>>


    @GET("/parameter-results/all")
    fun getResult(
        @Header("Authorization") token: String
    ): Single<Response<List<ResultDto>>>

}