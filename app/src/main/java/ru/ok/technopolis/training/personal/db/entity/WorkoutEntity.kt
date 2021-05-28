package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = WorkoutCategoryEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["categoryId"],
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class WorkoutEntity(
    @ColumnInfo var name: String,
    @ColumnInfo var description: String,
    @ColumnInfo var categoryId: Long,
    @ColumnInfo var sport: String,
    @ColumnInfo var difficulty: Int,
    @ColumnInfo var isPublic: Boolean,
    @ColumnInfo var authorId: Long,
    @ColumnInfo var redactorId: Long?,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
