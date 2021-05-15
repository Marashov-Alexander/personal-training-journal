package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity

@Dao
interface ExerciseParameterDao {
    @Query("SELECT * FROM ExerciseParameterEntity")
    fun getAll(): List<ExerciseParameterEntity>

    @Query("SELECT * FROM ExerciseParameterEntity WHERE exerciseId=:exerciseId AND parameterId=:parameterId")
    fun getById(exerciseId: Long, parameterId: Long): ExerciseParameterEntity

    @Query("SELECT * FROM ExerciseParameterEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): ExerciseParameterEntity

    @Query("SELECT * FROM ExerciseParameterEntity WHERE exerciseId=:exerciseId")
    fun getAllByExercise(exerciseId: Long): List<ExerciseParameterEntity>

    @Query("SELECT * FROM ParameterEntity AS pe " +
        "INNER JOIN ExerciseParameterEntity AS epe ON pe.id = epe.parameterId " +
        "WHERE epe.exerciseId = :exerciseId AND epe.deleted=0")
    fun getParametersForExercise(exerciseId: Long): List<ParameterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exerciseParameterEntity: ExerciseParameterEntity): Long

    @Insert
    fun insert(exerciseParameterEntity: List<ExerciseParameterEntity>): List<Long>

    @Update
    fun update(exerciseParameterEntity: ExerciseParameterEntity): Int

    @Update
    fun update(exerciseParameterEntityList: List<ExerciseParameterEntity>): Int

    @Delete
    fun delete(exerciseParameterEntity: ExerciseParameterEntity): Int

    @Query("DELETE FROM ExerciseParameterEntity WHERE exerciseId=:exerciseId AND parameterId=:parameterId")
    fun delete(exerciseId: Long, parameterId: Long): Int
}