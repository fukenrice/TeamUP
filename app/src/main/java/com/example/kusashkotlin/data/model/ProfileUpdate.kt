package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class ProfileUpdate(
    @SerializedName("remote")
    var remote: Int? = null,

    @SerializedName("is_male")
    var isMale: Boolean? = null,

    @SerializedName("specialization")
    var specialization: List<Int>? = null,


    @SerializedName("patronymic")
    var patronymic: String? = null,

    @SerializedName("city")
    var city: String?= null,

    @SerializedName("age")
    var age: Int? = null, // а тут уже Int

    @SerializedName("description")
    var description: String?= null,

    @SerializedName("user")
    var user: User?= null,
)
