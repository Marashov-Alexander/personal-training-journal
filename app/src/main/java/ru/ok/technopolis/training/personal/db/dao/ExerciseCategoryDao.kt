package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ExerciseCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity

@Dao
interface ExerciseCategoryDao {
    @Query("SELECT * FROM ExerciseCategoryEntity")
    fun getAll(): List<ExerciseCategoryEntity>

    @Query("SELECT * FROM ExerciseCategoryEntity WHERE id=:id")
    fun getById(id: Long): ExerciseCategoryEntity

    @Query("SELECT * FROM ExerciseCategoryEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): ExerciseCategoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exerciseCategoryEntity: ExerciseCategoryEntity): Long

    @Insert
    fun insert(exerciseCategoryEntityList: List<ExerciseCategoryEntity>): List<Long>

    @Update
    fun update(exerciseCategoryEntity: ExerciseCategoryEntity): Int

    @Update
    fun update(exerciseCategoryEntity: List<ExerciseCategoryEntity>): Int

    @Delete
    fun delete(exerciseCategoryEntity: ExerciseCategoryEntity): Int
}