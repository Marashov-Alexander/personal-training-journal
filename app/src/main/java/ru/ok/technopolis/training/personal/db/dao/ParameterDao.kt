package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity

@Dao
interface ParameterDao {
    @Query("SELECT * FROM ParameterEntity")
    fun getAll(): List<ParameterEntity>

    @Query("SELECT * FROM ParameterEntity WHERE id = :id")
    fun getById(id: Long): ParameterEntity

    @Query("SELECT * FROM ParameterEntity WHERE serverId = :id")
    fun getByServerId(id: Long): ParameterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parameterEntity: ParameterEntity): Long

    @Insert
    fun insert(parameterEntityList: List<ParameterEntity>): List<Long>

    @Update
    fun update(parameterEntity: ParameterEntity): Int

    @Update
    fun update(parameterEntityList: List<ParameterEntity>): Int

    @Delete
    fun delete(parameterEntity: ParameterEntity): Int
}