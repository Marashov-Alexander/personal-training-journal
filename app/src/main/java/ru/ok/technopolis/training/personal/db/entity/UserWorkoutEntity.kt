package ru.ok.technopolis.training.personal.db.entity

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = CASCADE
        )
    ]
)
data class UserWorkoutEntity(
    @ColumnInfo var userId: Long,
    @ColumnInfo var workoutId: Long,
    @ColumnInfo var accepted: Boolean,
    @ColumnInfo var timestampFrom: Long, // точка отсчёта расписания. По умолчанию - время создания
    @ColumnInfo var restDays: Int? = null,
    @ColumnInfo var weekdaysMask: Int? = null,
    @ColumnInfo var plannedTimes: String = "",
    @ColumnInfo var notifyMinutes: Int = 0,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }

    fun getPlannedTime(dayIndex: Int): String {
        if (!isScheduled())
            return ""
        return plannedTimes.split("*").getOrElse(dayIndex) { "" }
    }

    fun isScheduled(): Boolean {
        return restDays != null || weekdaysMask != null
    }

    fun getWeekdaysArray(): BooleanArray? {
        weekdaysMask?.let { mask ->
            val result = BooleanArray(7) { false }
            for (i in 0..6) {
                val bit = 1 shl i
                result[i] = bit and mask != 0
            }
            return result
        }
        return null
    }

    fun setWeekdaysMask(value: BooleanArray) {
        var weekdaysMask = 0
        for (i in value.indices) {
            if (value[i]) {
                weekdaysMask = weekdaysMask or (1 shl i)
            }
        }
        this.weekdaysMask = weekdaysMask
    }

}
