package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Date

data class DayItem(
    override val id: String,
    var date: Date,
    var name: String,
    var isChosen: Boolean,
    var isToday: Boolean,
    var event: EventColor
) : WithId

enum class EventColor { RED, GREEN, WHITE, NONE }

