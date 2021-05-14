package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity

@Dao
interface UserWorkoutDao {
    @Query("SELECT * FROM UserWorkoutEntity")
    fun getAll(): List<UserWorkoutEntity>

    @Query("SELECT * FROM UserWorkoutEntity WHERE userId=:userId AND workoutId=:workoutId")
    fun getById(userId: Long, workoutId: Long): UserWorkoutEntity

    @Query("SELECT * FROM UserWorkoutEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): UserWorkoutEntity

    @Query("SELECT * FROM WorkoutEntity AS we " +
        "INNER JOIN UserWorkoutEntity AS uwe ON we.id=uwe.workoutId " +
        "WHERE uwe.userId = :userId AND we.serverId=-1")
    fun getUnsavedWorkoutsForUser(userId: Long): MutableList<WorkoutEntity>

    @Query("SELECT * FROM WorkoutEntity AS we " +
        "INNER JOIN UserWorkoutEntity AS uwe ON we.id=uwe.workoutId " +
        "WHERE uwe.userId = :userId AND deleted=0")
    fun getWorkoutsForUser(userId: Long): MutableList<WorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userWorkoutEntity: UserWorkoutEntity): Long

    @Insert
    fun insert(userWorkoutEntityList: List<UserWorkoutEntity>): List<Long>

    @Update
    fun update(userWorkoutEntity: UserWorkoutEntity): Int

    @Update
    fun update(userWorkoutEntityList: List<UserWorkoutEntity>): Int

    @Delete
    fun delete(userWorkoutEntity: UserWorkoutEntity): Int
}