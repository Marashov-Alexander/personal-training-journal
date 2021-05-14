package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WorkoutExerciseDto(
    @SerializedName("workout_id")
    @Expose
    val workoutId: Long,
    @SerializedName("exercise_id")
    @Expose
    val exerciseId: Long
)