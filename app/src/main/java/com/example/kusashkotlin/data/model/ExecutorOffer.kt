package com.example.kusashkotlin.data.model

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName

data class ExecutorOffer(
    @SerializedName("description")
    var description: String = "",

    @SerializedName("salary")
    var salary: Int = 0,

    @SerializedName("work_hours")
    var workHours: String = ""
)
