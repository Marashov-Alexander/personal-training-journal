package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = DoneExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["doneExerciseId"]
        ),
        ForeignKey(
            entity = ParameterEntity::class,
            parentColumns = ["id"],
            childColumns = ["parameterId"]
        )
    ]
)
data class ParameterResultEntity(
    @ColumnInfo var doneExerciseId: Long,
    @ColumnInfo var parameterId: Long,
    @ColumnInfo var value: Float,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
