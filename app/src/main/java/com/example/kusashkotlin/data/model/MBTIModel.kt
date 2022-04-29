package com.example.kusashkotlin.data.model

import com.google.gson.annotations.SerializedName

data class MBTIModel(
    @SerializedName("value")
    var value: List<MBTIQuestions> = listOf(
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
        MBTIQuestions(),
    )
)
