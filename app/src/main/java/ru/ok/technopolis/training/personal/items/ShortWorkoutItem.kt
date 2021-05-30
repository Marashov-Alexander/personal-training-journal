package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Time

data class ShortWorkoutItem(
        override val id: String,
//        var timeStart: Time,
        var workout: WorkoutEntity,
//        var description: String,
        var category: String,
        var sport: String,
//        var duration: String,
        val downloadsNumber: Int,
        val rank: Double
//        val private: Boolean,
//        val invisible: Boolean
): WithId