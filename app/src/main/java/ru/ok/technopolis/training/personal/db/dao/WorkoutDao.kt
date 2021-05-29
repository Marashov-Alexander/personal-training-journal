package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM WorkoutEntity")
    fun getAll(): MutableList<WorkoutEntity>

    @Query("SELECT * FROM WorkoutEntity WHERE id = :id")
    fun getById(id: Long): WorkoutEntity

    @Query("SELECT * FROM WorkoutEntity WHERE serverId = :id")
    fun getByServerId(id: Long): WorkoutEntity

    @Query("SELECT * FROM WorkoutEntity WHERE categoryId = :categoryId")
    fun getByCategoryId(categoryId: Long): List<WorkoutEntity>

    @Query("SELECT * FROM WorkoutEntity WHERE categoryId = :categoryId and authorId = :authorId")
    fun getByCategoryIdAndAuthorId(categoryId: Long, authorId: Long): List<WorkoutEntity>

    @Query("SELECT * FROM WorkoutEntity WHERE categoryId = :categoryId and authorId <> :authorId")
    fun getByCategoryIdNotForAuthor(categoryId: Long, authorId: Long): List<WorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workoutEntity: WorkoutEntity): Long

    @Insert
    fun insert(workoutEntityList: List<WorkoutEntity>): List<Long>

    @Update
    fun update(workoutEntity: WorkoutEntity): Int

    @Update
    fun update(workoutEntityList: List<WorkoutEntity>): Int

    @Delete
    fun delete(workoutEntity: WorkoutEntity): Int
}