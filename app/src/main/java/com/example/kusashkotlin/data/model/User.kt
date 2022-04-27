package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("username")
    var username: String = "",

    @SerializedName("first_name")
    var firstName: String = "",

    @SerializedName("last_name")
    var lastName: String = "",

    @SerializedName("email")
    var email: String = ""
)
