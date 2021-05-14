package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExerciseParameterDto(
    @SerializedName("exercise_id")
    @Expose
    val exerciseId: Long,
    @SerializedName("parameter_id")
    @Expose
    val parameterId: Long
)