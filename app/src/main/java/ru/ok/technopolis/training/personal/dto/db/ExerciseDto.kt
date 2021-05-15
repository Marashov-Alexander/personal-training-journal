package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExerciseDto(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("exercise_type_id")
    @Expose
    val typeId: Long
)