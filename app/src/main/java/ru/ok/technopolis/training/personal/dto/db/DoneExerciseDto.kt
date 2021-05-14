package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class DoneExerciseDto(
    @SerializedName("exercise_id")
    @Expose
    val exerciseId: Long,
    @SerializedName("date")
    @Expose
    val date: String
)
