package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ExerciseMediaEntity

@Dao
interface ExerciseMediaDao {
    @Query("SELECT * FROM ExerciseMediaEntity")
    fun getAll(): MutableList<ExerciseMediaEntity>

    @Query("SELECT * FROM ExerciseMediaEntity WHERE id = :id")
    fun getById(id: Long): ExerciseMediaEntity

    @Query("SELECT * FROM ExerciseMediaEntity WHERE exerciseId = :exerciseId")
    fun getByExerciseId(exerciseId: Long): List<ExerciseMediaEntity>

    @Query("SELECT * FROM ExerciseMediaEntity WHERE serverId = :id")
    fun getByServerId(id: Long): ExerciseMediaEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exerciseMediaEntity: ExerciseMediaEntity): Long

    @Insert
    fun insert(exerciseMediaEntity: List<ExerciseMediaEntity>): List<Long>

    @Update
    fun update(exerciseMediaEntity: ExerciseMediaEntity): Int

    @Update
    fun update(exerciseMediaEntity: List<ExerciseMediaEntity>): Int

    @Query("DELETE FROM ExerciseMediaEntity WHERE ExerciseMediaEntity.exerciseId = :exerciseId")
    fun deleteByExercise(exerciseId: Long): Int

    @Delete
    fun delete(exerciseMediaEntity: ExerciseMediaEntity): Int
}