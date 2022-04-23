package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("emai")
    var email: String = "",

    @SerializedName("username")
    var username: String = "",

    @SerializedName("id")
    var id: String = "",

    @SerializedName("password")
    var password: String = "",
)
