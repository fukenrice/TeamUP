package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class ProjectModel(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("team")
    var team: List<Int>? = null,

    @SerializedName("title")
    var title: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("vacant")
    var vacant: Int? = 0,

    @SerializedName("city")
    var city: String? = null,

    @SerializedName("online")
    var online: Boolean? = null,

    @SerializedName("verified")
    var verified: Boolean? = null,

    @SerializedName("owner")
    var owner: Int? = null,

    @SerializedName("required_specialization")
    var requiredSpecialization: List<Int> = listOf(),

    @SerializedName("required_belbin")
    var requiredBelbin: List<Int> = listOf()
)
