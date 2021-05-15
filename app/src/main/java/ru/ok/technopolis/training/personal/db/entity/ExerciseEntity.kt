package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ExerciseTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["typeId"]
        )
    ]
)
data class ExerciseEntity(
    @ColumnInfo var name: String,
    @ColumnInfo var description: String?,
    @ColumnInfo var typeId: Long,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun toString(): String {
        return name
    }

    override fun serverId(newId: Long) {
        serverId = newId
    }
}
