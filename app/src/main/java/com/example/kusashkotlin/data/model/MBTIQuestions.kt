package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

// Хочется понять разницу с BelbinModel, из того что вижу только отсутсвие answer0
data class MBTIQuestions(
    @SerializedName("answer1")
    var answer1: Int? = null,

    @SerializedName("answer2")
    var answer2: Int = 0,

    @SerializedName("answer3")
    var answer3: Int = 0,

    @SerializedName("answer4")
    var answer4: Int = 0,

    @SerializedName("answer5")
    var answer5: Int = 0,

    @SerializedName("answer6")
    var answer6: Int = 0,

    @SerializedName("answer7")
    var answer7: Int = 0,
)
