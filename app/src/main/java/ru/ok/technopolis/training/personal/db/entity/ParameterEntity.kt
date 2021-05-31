package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParameterEntity(
    @ColumnInfo var name: String,
    @ColumnInfo var measureUnit: String,
    @ColumnInfo var resultType: Int = GREATER_BETTER,
    @ColumnInfo var input: Int = INPUT_SIMPLE,
    @ColumnInfo var showInDescription: Boolean = false,
    @ColumnInfo var parameterType: Int = PARAMETER_ORDINARY,
    @ColumnInfo var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : WithServerId {
    override fun serverId(newId: Long) {
        serverId = newId
    }
    companion object {
        const val INPUT_SIMPLE = 0
        const val INPUT_STOPWATCH = 1
        const val INPUT_TIMER = 2

        const val GREATER_BETTER = 1
        const val EQUALS_BETTER = 2
        const val LESS_BETTER = 3

        const val PARAMETER_ORDINARY = 0
        const val PARAMETER_REPEATS = 1
        const val PARAMETER_REST = 2
    }
}
