package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["level", "exerciseParameterId"],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseParameterEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseParameterId"],
            onDelete = CASCADE
        )
    ]
)
data class LevelExerciseParameterEntity(
    @ColumnInfo var level: Int,
    @ColumnInfo var value: Float,
    @ColumnInfo var exerciseParameterId: Long,
    @ColumnInfo var serverId: Long = -1L
) : WithServerId {
    constructor(prevVersion: LevelExerciseParameterEntity, newLevel: Int) : this(
        newLevel,
        prevVersion.value,
        prevVersion.exerciseParameterId,
        prevVersion.serverId
    )

    override fun serverId(newId: Long) {
        serverId = newId
    }
}
