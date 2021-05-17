package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

open class SelectableItem (
    override val id: String,
    open var isChosen: Boolean = false
) : WithId
