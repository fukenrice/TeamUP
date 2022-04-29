package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class BelbinQuestions(
    @SerializedName("answer0")
    var answer0: Int = 0,

    @SerializedName("answer1")
    var answer1: Int = 0,

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
