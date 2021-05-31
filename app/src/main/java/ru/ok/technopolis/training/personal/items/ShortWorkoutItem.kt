package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class ShortWorkoutItem(
        override val id: String,
        var workout: WorkoutEntity,
        var category: String,
        var sport: String,
        val downloadsNumber: Int,
        val rank: Double,
        val pictureUrl: String
): WithId