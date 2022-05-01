package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class RoleModel(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("role")
    var role: String = ""
)
