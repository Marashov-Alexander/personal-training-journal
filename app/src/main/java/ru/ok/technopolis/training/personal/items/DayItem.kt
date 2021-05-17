package ru.ok.technopolis.training.personal.items

import java.util.Date

data class DayItem(
    override val id: String,
    var date: Date,
    var name: String,
    var isToday: Boolean,
    var event: EventColor,
    override var isChosen: Boolean = false
) : SelectableItem(id, isChosen)

enum class EventColor { RED, GREEN, WHITE, NONE }

