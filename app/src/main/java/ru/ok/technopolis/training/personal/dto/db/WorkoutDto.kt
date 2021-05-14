package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WorkoutDto(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("planned_time")
    @Expose
    val plannedTime: String,
    @SerializedName("weekdays_mask")
    @Expose
    val weekdaysMask: Int
)