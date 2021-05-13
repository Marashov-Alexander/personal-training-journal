package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class ShortParameterItem(
    override val id: String,
    val name: String,
    val unitName: String,
    val value: Int,
    val iconId: Int,
    val editable: Boolean,
    val invisible: Boolean = false
) : WithId
