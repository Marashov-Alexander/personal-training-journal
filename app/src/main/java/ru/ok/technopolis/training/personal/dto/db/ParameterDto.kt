package ru.ok.technopolis.training.personal.dto.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParameterDto(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("measure_unit")
    @Expose
    val measureUnit: String,
    @SerializedName("result_type")
    @Expose
    val resultType: Int,
    @SerializedName("value")
    @Expose
    val value: Float
)