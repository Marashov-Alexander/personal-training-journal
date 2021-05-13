package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class CategoryWorkoutsItem (
        override val id: String,
        var name: String,
        var worlouts: List<ShortWorkoutItem>
): WithId