package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["exerciseId", "parameterId"],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ParameterEntity::class,
            parentColumns = ["id"],
            childColumns = ["parameterId"],
            onDelete = CASCADE
        )
    ]
)
data class ExerciseParameterEntity(
    @ColumnInfo var exerciseId: Long,
    @ColumnInfo var parameterId: Long,
    @ColumnInfo var serverId: Long = -1L,
    @ColumnInfo var deleted: Boolean = false
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
