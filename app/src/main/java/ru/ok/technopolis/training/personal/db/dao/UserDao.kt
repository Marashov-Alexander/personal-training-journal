package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    fun getById(id: Long): UserEntity

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    fun getByEmail(email: String): UserEntity

    @Query("SELECT * FROM UserEntity WHERE id <> :userId")
    fun getAllExceptUser(userId: Long): List<UserEntity>

    @Query("SELECT * FROM WorkoutEntity WHERE id in (SELECT workoutId FROM UserWorkoutEntity where userId = :id)")
    fun getUserWorkouts(id: Long): List<WorkoutEntity>

    @Query("SELECT * FROM ExerciseEntity WHERE id in (SELECT exerciseId FROM UserExerciseEntity where userId = :id)")
    fun getUserExercises(id: Long): List<ExerciseEntity>

    @Query(" SELECT * FROM WorkoutSportEntity WHERE id in( SELECT sportId FROM WorkoutEntity WHERE id in ( SELECT workoutId FROM UserWorkoutEntity where userId = :id))")
    fun getUserSports(id: Long): List<WorkoutSportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity): Long

    @Insert
    fun insert(userList: List<UserEntity>): List<Long>

    @Update
    fun update(user: UserEntity): Int

    @Update
    fun update(userList: List<UserEntity>): Int

    @Delete
    fun delete(user: UserEntity): Int
}