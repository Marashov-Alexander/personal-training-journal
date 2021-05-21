package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Time

data class ShortWorkoutItem(
        override val id: String,
        var timeStart: Time,
        var name: String,
        var category: String,
        var sport: String,
        var duration: String,
        val done: Boolean,
        val downloadsNumber: Int,
        val rank: Double,
        val private: Boolean,
        val invisible: Boolean
): WithId