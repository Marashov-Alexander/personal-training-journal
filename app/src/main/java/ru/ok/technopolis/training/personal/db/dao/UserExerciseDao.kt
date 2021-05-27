package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.UserExerciseEntity

@Dao
interface UserExerciseDao {
    @Query("SELECT * FROM UserExerciseEntity")
    fun getAll(): List<UserExerciseEntity>

    @Query("SELECT * FROM UserExerciseEntity WHERE userId=:userId AND exerciseId = :exerciseId")
    fun getById(userId: Long, exerciseId: Long): UserExerciseEntity

    @Query("SELECT * FROM UserExerciseEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): UserExerciseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(UserExerciseEntity: UserExerciseEntity): Long

    @Insert
    fun insert(UserExerciseEntityList: List<UserExerciseEntity>): List<Long>

    @Update
    fun update(UserExerciseEntity: UserExerciseEntity): Int

    @Update
    fun update(UserExerciseEntityList: List<UserExerciseEntity>): Int

    @Delete
    fun delete(UserExerciseEntity: UserExerciseEntity): Int
}