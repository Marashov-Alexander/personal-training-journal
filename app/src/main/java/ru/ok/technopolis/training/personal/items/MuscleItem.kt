package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

class MuscleItem(
    override val id: String,
    val name: String,
    var checked: Boolean
) : WithId
