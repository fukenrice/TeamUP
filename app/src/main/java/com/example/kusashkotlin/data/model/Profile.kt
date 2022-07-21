package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

// Вообще модели дучше делать immutable, чтобы нельзя было значения менять. так безопаснее.
data class Profile(
    @SerializedName("id")
    var id: Int = 0, // в этом случае профайл валидный буудет? Если нет лучше на задавать для полей типа id значения по умолчанию.

    @SerializedName("remote")
    var remote: String = "",

    @SerializedName("sex")
    var sex: String = "",

    @SerializedName("user")
    var user: User,

    @SerializedName("executor_offer")
    var executorOffer: ExecutorOffer? = null,

    @SerializedName("belbin")
    var belbin: ArrayList<String>? = null,

    @SerializedName("mbti")
    var mbti: List<String>? = null,

    @SerializedName("lsq")
    var lsq: List<String>? = null,

    @SerializedName("specialization")
    var specialzation: List<String>? = null,

    @SerializedName("patronymic")
    var patronymic: String = "",

    @SerializedName("photo")
    var photo: String = "", // это url? тогда лучше назвать photoUrl - будет понятнее

    @SerializedName("description")
    var desctiption: String = "",

    @SerializedName("cv")
    var cv: String? = null,

    @SerializedName("city")
    var city: String = "",

    @SerializedName("age")
    var age: String = "", // String?

    @SerializedName("verified")
    var verified: Boolean = false,

    @SerializedName("project")
    var project: String = ""
)
