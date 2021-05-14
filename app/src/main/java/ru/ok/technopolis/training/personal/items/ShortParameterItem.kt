package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class ShortParameterItem(
    override val id: String,
    val parameter: ParameterEntity?,
    var editable: Boolean,
    var invisible: Boolean = false
) : WithId
