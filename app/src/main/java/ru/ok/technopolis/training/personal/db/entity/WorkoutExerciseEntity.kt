package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = CASCADE
        )
    ]
)
data class WorkoutExerciseEntity(
    @ColumnInfo var workoutId: Long,
    @ColumnInfo var exerciseId: Long,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
