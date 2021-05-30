package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity

@Dao
interface WorkoutSportDao {
    @Query("SELECT * FROM WorkoutSportEntity")
    fun getAll(): List<WorkoutSportEntity>

    @Query("SELECT * FROM WorkoutSportEntity WHERE id=:id")
    fun getById(id: Long): WorkoutSportEntity

    @Query("SELECT * FROM WorkoutSportEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): WorkoutSportEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workoutSportEntity: WorkoutSportEntity): Long

    @Insert
    fun insert(workoutSportEntityList: List<WorkoutSportEntity>): List<Long>

    @Update
    fun update(workoutSportEntity: WorkoutSportEntity): Int

    @Update
    fun update(workoutSportEntity: List<WorkoutSportEntity>): Int

    @Delete
    fun delete(workoutSportEntity: WorkoutSportEntity): Int
}