package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"]
        ),
        ForeignKey(
            entity = ExerciseParameterEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseParameterId"]
        ),
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"]
        )
    ]
)
data class ParameterResultEntity(
    @ColumnInfo var userId: Long,
    @ColumnInfo var exerciseParameterId: Long,
    @ColumnInfo var workoutId: Long,
    @ColumnInfo var timestamp: Long,
    @ColumnInfo var goalValue: Float,
    @ColumnInfo var value: Float,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
