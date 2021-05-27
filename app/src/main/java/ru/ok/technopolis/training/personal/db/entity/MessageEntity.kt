package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MessageEntity (
        @ColumnInfo var text: String,
        @ColumnInfo var sendTime: Date,
        @ColumnInfo var senderId: Long,
        @ColumnInfo var chatId: Long,
        @ColumnInfo var requestId: Long?,
        @ColumnInfo var workoutId: Long?,
        @ColumnInfo var exerciseId: Long?,
        @ColumnInfo var read: Boolean,
        @ColumnInfo var serverId: Long = -1L,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}

