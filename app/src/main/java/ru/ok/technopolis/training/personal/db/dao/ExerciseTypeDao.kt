package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ExerciseTypeEntity

@Dao
interface ExerciseTypeDao {
    @Query("SELECT * FROM ExerciseTypeEntity")
    fun getAll(): List<ExerciseTypeEntity>

    @Query("SELECT * FROM ExerciseTypeEntity WHERE id = :id")
    fun getById(id: Long): ExerciseTypeEntity

    @Query("SELECT * FROM ExerciseTypeEntity WHERE serverId = :id")
    fun getByServerId(id: Long): ExerciseTypeEntity

    @Insert
    fun insert(exerciseTypeEntity: ExerciseTypeEntity): Long

    @Insert
    fun insert(exerciseTypeEntityList: List<ExerciseTypeEntity>): List<Long>

    @Update
    fun update(exerciseTypeEntity: ExerciseTypeEntity): Int

    @Update
    fun update(exerciseTypeEntityList: List<ExerciseTypeEntity>): Int

    @Delete
    fun delete(exerciseTypeEntity: ExerciseTypeEntity): Int
}