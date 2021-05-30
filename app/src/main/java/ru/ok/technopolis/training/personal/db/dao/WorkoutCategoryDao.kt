package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity

@Dao
interface WorkoutCategoryDao {
    @Query("SELECT * FROM WorkoutCategoryEntity")
    fun getAll(): List<WorkoutCategoryEntity>

    @Query("SELECT * FROM WorkoutCategoryEntity WHERE id=:id")
    fun getById(id: Long): WorkoutCategoryEntity

    @Query("SELECT * FROM WorkoutCategoryEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): WorkoutCategoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workoutCategoryEntity: WorkoutCategoryEntity): Long

    @Insert
    fun insert(workoutCategoryEntityList: List<WorkoutCategoryEntity>): List<Long>

    @Update
    fun update(workoutCategoryEntity: WorkoutCategoryEntity): Int

    @Update
    fun update(workoutCategoryEntity: List<WorkoutCategoryEntity>): Int

    @Delete
    fun delete(workoutCategoryEntity: WorkoutCategoryEntity): Int
}