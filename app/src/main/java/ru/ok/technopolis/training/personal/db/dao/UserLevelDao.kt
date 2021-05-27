package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.UserLevelEntity

@Dao
interface UserLevelDao {
    @Query("SELECT * FROM UserLevelEntity")
    fun getAll(): List<UserLevelEntity>

    @Query("SELECT * FROM UserLevelEntity WHERE userId=:userId AND exerciseWorkoutId = :exerciseWorkoutId")
    fun getById(userId: Long, exerciseWorkoutId: Long): UserLevelEntity?

    @Query("SELECT * FROM UserLevelEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): UserLevelEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(UserLevelEntity: UserLevelEntity): Long

    @Insert
    fun insert(UserLevelEntityList: List<UserLevelEntity>): List<Long>

    @Update
    fun update(UserLevelEntity: UserLevelEntity): Int

    @Update
    fun update(UserLevelEntityList: List<UserLevelEntity>): Int

    @Delete
    fun delete(UserLevelEntity: UserLevelEntity): Int
}