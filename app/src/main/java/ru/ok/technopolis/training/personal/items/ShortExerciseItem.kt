package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class ShortExerciseItem (
        override val id: String,
        var exercise: ExerciseEntity,
        var category: String,
        val downloadsNumber: Int,
        val rank: Double,
        var url: String
): WithId