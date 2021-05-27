package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["userExerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserWorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["userWorkoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MessageEntity (
        @ColumnInfo var text: String,
        @ColumnInfo var timestamp: Long,
        @ColumnInfo var senderId: Long,
        @ColumnInfo var recipientId: Long,
        @ColumnInfo var userWorkoutId: Long?,
        @ColumnInfo var userExerciseId: Long?,
        @ColumnInfo var serverId: Long = -1L,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}

