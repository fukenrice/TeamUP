package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class BelbinModel(
    @SerializedName("value")
    var value: List<BelbinQuestions> = listOf(
        BelbinQuestions(),
        BelbinQuestions(),
        BelbinQuestions(),
        BelbinQuestions(),
        BelbinQuestions(),
        BelbinQuestions(),
        BelbinQuestions()
    )
)