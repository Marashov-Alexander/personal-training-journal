package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["userId", "workoutId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = CASCADE
        )
    ]
)
data class UserWorkoutEntity(
    @ColumnInfo var userId: Long,
    @ColumnInfo var workoutId: Long,
    @ColumnInfo var comments: String?,
    @ColumnInfo var serverId: Long = -1L,
    @ColumnInfo var deleted: Boolean = false
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
