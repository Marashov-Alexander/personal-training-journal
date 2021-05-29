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
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseEntity(
    @ColumnInfo var name: String,
    @ColumnInfo var description: String?,
    @ColumnInfo var category: String?,
    @ColumnInfo var isPublic: Boolean,
    @ColumnInfo var authorId: Long,
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
