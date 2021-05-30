package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.SubscriptionEntity

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM SubscriptionEntity")
    fun getAll(): List<SubscriptionEntity>

    @Query("SELECT * FROM SubscriptionEntity WHERE id = :id")
    fun getById(id: Long): SubscriptionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: SubscriptionEntity): Long

    @Insert
    fun insert(userList: List<SubscriptionEntity>): List<Long>

    @Update
    fun update(user: SubscriptionEntity): Int

    @Update
    fun update(userList: List<SubscriptionEntity>): Int

    @Delete
    fun delete(user: SubscriptionEntity): Int
}