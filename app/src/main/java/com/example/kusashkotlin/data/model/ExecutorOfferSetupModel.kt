package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class ExecutorOfferSetupModel(
    @SerializedName("description")
    var description: String? = "",

    @SerializedName("salary")
    var salary: Int? = 0,

    @SerializedName("work_hours")
    var workHours: Int? = 0
)
