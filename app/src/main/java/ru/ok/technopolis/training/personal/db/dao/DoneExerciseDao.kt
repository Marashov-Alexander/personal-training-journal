package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.DoneExerciseEntity

@Dao
interface DoneExerciseDao {
    @Query("SELECT * FROM DoneExerciseEntity")
    fun getAll(): List<DoneExerciseEntity>

    @Query("SELECT * FROM DoneExerciseEntity WHERE userId=:userId")
    fun getAllByUser(userId: Long): List<DoneExerciseEntity>

    @Query("SELECT * FROM DoneExerciseEntity WHERE userId=:userId AND serverId=-1")
    fun getAllUnsavedByUser(userId: Long): List<DoneExerciseEntity>

    @Query("SELECT * FROM DoneExerciseEntity WHERE id = :id")
    fun getById(id: Long): DoneExerciseEntity

    @Query("SELECT * FROM DoneExerciseEntity WHERE serverId = :serverId")
    fun getByServerId(serverId: Long): DoneExerciseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(doneExerciseEntity: DoneExerciseEntity): Long

    @Insert
    fun insert(doneExerciseEntityList: List<DoneExerciseEntity>): List<Long>

    @Update
    fun update(doneExerciseEntity: DoneExerciseEntity): Int

    @Update
    fun update(doneExerciseEntity: List<DoneExerciseEntity>): Int

    @Delete
    fun delete(doneExerciseEntity: DoneExerciseEntity): Int
}