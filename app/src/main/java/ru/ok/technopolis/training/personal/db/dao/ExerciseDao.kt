package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM ExerciseEntity")
    fun getAll(): List<ExerciseEntity>

    @Query("SELECT * FROM ExerciseEntity WHERE id = :id")
    fun getById(id: Long): ExerciseEntity

    @Query("SELECT * FROM ExerciseEntity WHERE serverId = :id")
    fun getByServerId(id: Long): ExerciseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exerciseEntity: ExerciseEntity): Long

    @Insert
    fun insert(exerciseEntity: List<ExerciseEntity>): List<Long>

    @Update
    fun update(exerciseEntity: ExerciseEntity): Int

    @Update
    fun update(exerciseEntityList: List<ExerciseEntity>): Int

    @Delete
    fun delete(exerciseEntity: ExerciseEntity): Int
}