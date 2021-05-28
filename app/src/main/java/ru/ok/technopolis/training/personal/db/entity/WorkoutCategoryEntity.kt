package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class WorkoutCategoryEntity (
        @ColumnInfo var name: String,
        @ColumnInfo var serverId: Long = -1L,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun toString(): String {
        return name
    }

    override fun serverId(newId: Long) {
        serverId = newId
    }
}