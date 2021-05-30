package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Time

data class ScheduledWorkoutItem(
    override val id: String,
    val workout: WorkoutEntity,
    val userWorkout: UserWorkoutEntity,
    val category: WorkoutCategoryEntity,
    val sport: WorkoutSportEntity,
    val timeStart: String,
    val done: Boolean
) : WithId

