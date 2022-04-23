package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class TokenResponse (
    @SerializedName("auth_token")
    val token: String = "",
)