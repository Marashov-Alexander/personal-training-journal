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
                    childColumns = ["userId"],
                    onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                    entity = ExerciseParameterEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["exerciseParameterId"],
                    onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                    entity = WorkoutEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["workoutId"],
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class ResultEntity(
        @ColumnInfo var userId: Long,
        @ColumnInfo var exerciseParameterId: Long,
        @ColumnInfo var workoutId: Long,
        @ColumnInfo var goal: Float,
        @ColumnInfo var result: Float,
        @ColumnInfo var timestamp: Long,
        @ColumnInfo var serverId: Long = -1L,
        @PrimaryKey(autoGenerate = true) var id: Long = 0L
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
