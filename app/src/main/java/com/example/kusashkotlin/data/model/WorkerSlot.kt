package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class WorkerSlot(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("description")
    var description: String = "",

    @SerializedName("salary")
    var salary: Int? = null,

    @SerializedName("work_hours")
    var workingHours: Int? = null,

    @SerializedName("profile")
    var profile: Int? = null,

    @SerializedName("project")
    var project: Int? = null,

    @SerializedName("role")
    var roles: List<Int> = listOf(),

    @SerializedName("specialization")
    var specializations: List<Int> = listOf(),
)
