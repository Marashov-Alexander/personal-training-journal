package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = WorkoutEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["workoutId"],
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class WorkoutMediaEntity(
        @ColumnInfo var url: String,
        @ColumnInfo var isLocal: Boolean = true,
        @ColumnInfo var workoutId: Long,
        @ColumnInfo var serverId: Long = -1L,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun toString(): String {
        return url
    }

    override fun serverId(newId: Long) {
        serverId = newId
    }
}