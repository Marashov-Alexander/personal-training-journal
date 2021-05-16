package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM MessageEntity")
    fun getAll(): MutableList<MessageEntity>

    @Query("SELECT * FROM MessageEntity WHERE id = :id")
    fun getById(id: Long): MessageEntity

    @Query("SELECT * FROM MessageEntity WHERE serverId = :id")
    fun getByServerId(id: Long): MessageEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workoutEntity: MessageEntity): Long

    @Insert
    fun insert(workoutEntityList: List<MessageEntity>): List<Long>

    @Update
    fun update(workoutEntity: MessageEntity): Int

    @Update
    fun update(workoutEntityList: List<MessageEntity>): Int

    @Delete
    fun delete(workoutEntity: MessageEntity): Int
}