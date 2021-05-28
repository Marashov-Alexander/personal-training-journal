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
    fun insert(levelExerciseParameterEntity: LevelExerciseParameterEntity): Long

    @Insert
    fun insert(levelExerciseParameterEntityList: List<LevelExerciseParameterEntity>): List<Long>

    @Update
    fun update(levelExerciseParameterEntity: LevelExerciseParameterEntity): Int

    @Update
    fun update(levelExerciseParameterEntityList: List<LevelExerciseParameterEntity>): Int

    @Delete
    fun delete(levelExerciseParameterEntity: LevelExerciseParameterEntity): Int

    @Query("""
        DELETE 
            FROM LevelExerciseParameterEntity 
        WHERE 
            LevelExerciseParameterEntity.level > :level 
                AND EXISTS (
                    SELECT 
                        * 
                    FROM 
                        ExerciseParameterEntity 
                    WHERE 
                        ExerciseParameterEntity.id = LevelExerciseParameterEntity.exerciseParameterId 
                            AND ExerciseParameterEntity.exerciseId = :exerciseId
                )
    """)
    fun deleteLevelsGreaterThan(level: Int, exerciseId: Long): Int
}