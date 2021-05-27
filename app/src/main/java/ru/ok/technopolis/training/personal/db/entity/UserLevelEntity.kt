package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["userId", "exerciseWorkoutId"],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseWorkoutId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = CASCADE
        )
    ]
)
data class UserLevelEntity(
    @ColumnInfo var userId: Long,
    @ColumnInfo var exerciseWorkoutId: Long,
    @ColumnInfo var level: Int,
    @ColumnInfo var serverId: Long = -1L
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
