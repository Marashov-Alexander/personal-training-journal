package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultDto(
    @SerializedName("done_exercise_id")
    @Expose
    val doneExerciseId: Long,
    @SerializedName("parameter_id")
    @Expose
    val parameterId: Long,
    @SerializedName("value")
    @Expose
    val value: Float
)