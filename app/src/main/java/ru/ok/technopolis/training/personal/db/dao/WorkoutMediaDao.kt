package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.WorkoutMediaEntity

@Dao
interface WorkoutMediaDao {
    @Query("SELECT * FROM WorkoutMediaEntity")
    fun getAll(): MutableList<WorkoutMediaEntity>

    @Query("SELECT * FROM WorkoutMediaEntity WHERE id = :id")
    fun getById(id: Long): WorkoutMediaEntity

    @Query("SELECT * FROM WorkoutMediaEntity WHERE workoutId = :workoutId")
    fun getByWorkoutId(workoutId: Long): List<WorkoutMediaEntity>

    @Query("SELECT * FROM WorkoutMediaEntity WHERE serverId = :id")
    fun getByServerId(id: Long): WorkoutMediaEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workoutMediaEntity: WorkoutMediaEntity): Long

    @Insert
    fun insert(workoutMediaEntity: List<WorkoutMediaEntity>): List<Long>

    @Update
    fun update(workoutMediaEntity: WorkoutMediaEntity): Int

    @Update
    fun update(workoutMediaEntity: List<WorkoutMediaEntity>): Int

    @Query("DELETE FROM WorkoutMediaEntity WHERE WorkoutMediaEntity.workoutId = :workoutId")
    fun deleteByWorkout(workoutId: Long): Int

    @Delete
    fun delete(workoutMediaEntity: WorkoutMediaEntity): Int
}