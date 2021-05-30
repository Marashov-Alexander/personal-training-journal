package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Time

data class PlanDayItem (
        override val id: String,
        val name: String,
        var time: Time,
        var checked: Boolean
) : WithId