package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"]
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"]
        )
    ]
)
data class DoneExerciseEntity(
    @ColumnInfo var exerciseId: Long,
    @ColumnInfo var userId: Long,
    @ColumnInfo var date: Date,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
}
