package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("remote")
    var remote: String = "",

    @SerializedName("sex")
    var sex: String = "",

    @SerializedName("user")
    var user: User,

    @SerializedName("executor_offer")
    var executorOffer: ExecutorOffer? = null,

    @SerializedName("belbin")
    var belbin: List<String>? = null,

    @SerializedName("mbti")
    var mbti: List<String>? = null,

    @SerializedName("lsq")
    var lsq: List<String>? = null,

    @SerializedName("specialization")
    var specialzation: List<String>? = null,

    @SerializedName("patronymic")
    var patronymic: String = "",

    @SerializedName("photo")
    var photo: String = "",

    @SerializedName("description")
    var desctiption: String = "",

    @SerializedName("cv")
    var cv: String = "",

    @SerializedName("city")
    var city: String = "",

    @SerializedName("age")
    var age: String = "",

    @SerializedName("verified")
    var verified: Boolean = false
)
