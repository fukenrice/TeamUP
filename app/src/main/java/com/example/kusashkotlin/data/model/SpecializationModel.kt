package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class SpecializationModel(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String = ""

)
