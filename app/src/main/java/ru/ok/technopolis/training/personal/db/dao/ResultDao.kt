package ru.ok.technopolis.training.personal.db.dao

import androidx.room.*
import ru.ok.technopolis.training.personal.db.entity.ResultEntity

@Dao
interface ResultDao {
    @Query("SELECT * FROM ResultEntity")
    fun getAll(): List<ResultEntity>

    @Query("SELECT * FROM ResultEntity WHERE id = :id")
    fun getById(id: Long): ResultEntity

    @Query("SELECT * FROM ResultEntity WHERE workoutId = :workoutId AND userId = :userId")
    fun getByWorkoutAndUser(workoutId: Long, userId: Long): ResultEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: ResultEntity): Long

    @Insert
    fun insert(result: List<ResultEntity>): List<Long>

    @Update
    fun update(result: ResultEntity): Int

    @Update
    fun update(resultList: List<ResultEntity>): Int

    @Delete
    fun delete(result: ResultEntity): Int
}