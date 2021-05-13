package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class CategoryExerciseItem(
        override val id: String,
        var name: String,
        var exercises: List<ShortExerciseItem>
): WithId