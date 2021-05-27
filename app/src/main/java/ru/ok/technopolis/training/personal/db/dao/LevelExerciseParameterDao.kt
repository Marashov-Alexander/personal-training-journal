package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.LevelExerciseParameterEntity

@Dao
interface LevelExerciseParameterDao {
    @Query("SELECT * FROM LevelExerciseParameterEntity")
    fun getAll(): MutableList<LevelExerciseParameterEntity>

    @Query("SELECT * FROM LevelExerciseParameterEntity WHERE level = :level and exerciseParameterId = :exerciseParameterId")
    fun getById(level: Int, exerciseParameterId: Long): List<LevelExerciseParameterEntity>

    @Query("SELECT * FROM LevelExerciseParameterEntity WHERE exerciseParameterId = :exerciseParameterId")
    fun getAllByExerciseParameterId(exerciseParameterId: Long): MutableList<LevelExerciseParameterEntity>

    @Query("SELECT * FROM LevelExerciseParameterEntity WHERE serverId = :id")
    fun getByServerId(id: Long): LevelExerciseParameterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workoutEntity: LevelExerciseParameterEntity): Long

    @Insert
    fun insert(workoutEntityList: List<LevelExerciseParameterEntity>): List<Long>

    @Update
    fun update(workoutEntity: LevelExerciseParameterEntity): Int

    @Update
    fun update(workoutEntityList: List<LevelExerciseParameterEntity>): Int

    @Delete
    fun delete(workoutEntity: LevelExerciseParameterEntity): Int
}