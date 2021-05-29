package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Time

data class ShortExerciseItem (
        override val id: String,
        var name: String,
        var category: String,
        val downloadsNumber: Int,
        val rank: Double
): WithId