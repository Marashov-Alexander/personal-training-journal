package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserWorkoutDto(
    @SerializedName("workout_id")
    @Expose
    val workoutId: Long
)