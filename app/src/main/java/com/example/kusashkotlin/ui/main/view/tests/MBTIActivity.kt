package com.example.kusashkotlin.ui.main.view.tests

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.MBTIModel
import com.example.kusashkotlin.data.model.MBTIQuestions
import com.example.kusashkotlin.data.repo.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mbti.*

class MBTIActivity : AppCompatActivity() {

    private lateinit var mbti: MBTIModel

    private lateinit var save: SharedPreferences

    private var token: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        setContent()
    }

    @SuppressLint("CheckResult")
    private fun setContent() {
        setContentView(R.layout.activity_mbti)
        mbtiConfirmButton.setOnClickListener {
            mbti = MBTIModel(
                listOf(
                    MBTIQuestions(
                        mbtiBlock1Ans1RadioGroup.indexOfChild(mbtiBlock1Ans1RadioGroup.findViewById(mbtiBlock1Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock1Ans2RadioGroup.indexOfChild(mbtiBlock1Ans2RadioGroup.findViewById(mbtiBlock1Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock1Ans3RadioGroup.indexOfChild(mbtiBlock1Ans3RadioGroup.findViewById(mbtiBlock1Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock1Ans4RadioGroup.indexOfChild(mbtiBlock1Ans4RadioGroup.findViewById(mbtiBlock1Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock1Ans5RadioGroup.indexOfChild(mbtiBlock1Ans5RadioGroup.findViewById(mbtiBlock1Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock1Ans6RadioGroup.indexOfChild(mbtiBlock1Ans6RadioGroup.findViewById(mbtiBlock1Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock1Ans7RadioGroup.indexOfChild(mbtiBlock1Ans7RadioGroup.findViewById(mbtiBlock1Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock2Ans1RadioGroup.indexOfChild(mbtiBlock2Ans1RadioGroup.findViewById(mbtiBlock2Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock2Ans2RadioGroup.indexOfChild(mbtiBlock2Ans2RadioGroup.findViewById(mbtiBlock2Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock2Ans3RadioGroup.indexOfChild(mbtiBlock2Ans3RadioGroup.findViewById(mbtiBlock2Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock2Ans4RadioGroup.indexOfChild(mbtiBlock2Ans4RadioGroup.findViewById(mbtiBlock2Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock2Ans5RadioGroup.indexOfChild(mbtiBlock2Ans5RadioGroup.findViewById(mbtiBlock2Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock2Ans6RadioGroup.indexOfChild(mbtiBlock2Ans6RadioGroup.findViewById(mbtiBlock2Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock2Ans7RadioGroup.indexOfChild(mbtiBlock2Ans7RadioGroup.findViewById(mbtiBlock2Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock3Ans1RadioGroup.indexOfChild(mbtiBlock3Ans1RadioGroup.findViewById(mbtiBlock3Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock3Ans2RadioGroup.indexOfChild(mbtiBlock3Ans2RadioGroup.findViewById(mbtiBlock3Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock3Ans3RadioGroup.indexOfChild(mbtiBlock3Ans3RadioGroup.findViewById(mbtiBlock3Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock3Ans4RadioGroup.indexOfChild(mbtiBlock3Ans4RadioGroup.findViewById(mbtiBlock3Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock3Ans5RadioGroup.indexOfChild(mbtiBlock3Ans5RadioGroup.findViewById(mbtiBlock3Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock3Ans6RadioGroup.indexOfChild(mbtiBlock3Ans6RadioGroup.findViewById(mbtiBlock3Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock3Ans7RadioGroup.indexOfChild(mbtiBlock3Ans7RadioGroup.findViewById(mbtiBlock3Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock4Ans1RadioGroup.indexOfChild(mbtiBlock4Ans1RadioGroup.findViewById(mbtiBlock4Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock4Ans2RadioGroup.indexOfChild(mbtiBlock4Ans2RadioGroup.findViewById(mbtiBlock4Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock4Ans3RadioGroup.indexOfChild(mbtiBlock4Ans3RadioGroup.findViewById(mbtiBlock4Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock4Ans4RadioGroup.indexOfChild(mbtiBlock4Ans4RadioGroup.findViewById(mbtiBlock4Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock4Ans5RadioGroup.indexOfChild(mbtiBlock4Ans5RadioGroup.findViewById(mbtiBlock4Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock4Ans6RadioGroup.indexOfChild(mbtiBlock4Ans6RadioGroup.findViewById(mbtiBlock4Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock4Ans7RadioGroup.indexOfChild(mbtiBlock4Ans7RadioGroup.findViewById(mbtiBlock4Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock5Ans1RadioGroup.indexOfChild(mbtiBlock5Ans1RadioGroup.findViewById(mbtiBlock5Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock5Ans2RadioGroup.indexOfChild(mbtiBlock5Ans2RadioGroup.findViewById(mbtiBlock5Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock5Ans3RadioGroup.indexOfChild(mbtiBlock5Ans3RadioGroup.findViewById(mbtiBlock5Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock5Ans4RadioGroup.indexOfChild(mbtiBlock5Ans4RadioGroup.findViewById(mbtiBlock5Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock5Ans5RadioGroup.indexOfChild(mbtiBlock5Ans5RadioGroup.findViewById(mbtiBlock5Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock5Ans6RadioGroup.indexOfChild(mbtiBlock5Ans6RadioGroup.findViewById(mbtiBlock5Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock5Ans7RadioGroup.indexOfChild(mbtiBlock5Ans7RadioGroup.findViewById(mbtiBlock5Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock6Ans1RadioGroup.indexOfChild(mbtiBlock6Ans1RadioGroup.findViewById(mbtiBlock6Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock6Ans2RadioGroup.indexOfChild(mbtiBlock6Ans2RadioGroup.findViewById(mbtiBlock6Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock6Ans3RadioGroup.indexOfChild(mbtiBlock6Ans3RadioGroup.findViewById(mbtiBlock6Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock6Ans4RadioGroup.indexOfChild(mbtiBlock6Ans4RadioGroup.findViewById(mbtiBlock6Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock6Ans5RadioGroup.indexOfChild(mbtiBlock6Ans5RadioGroup.findViewById(mbtiBlock6Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock6Ans6RadioGroup.indexOfChild(mbtiBlock6Ans6RadioGroup.findViewById(mbtiBlock6Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock6Ans7RadioGroup.indexOfChild(mbtiBlock6Ans7RadioGroup.findViewById(mbtiBlock6Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock7Ans1RadioGroup.indexOfChild(mbtiBlock7Ans1RadioGroup.findViewById(mbtiBlock7Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock7Ans2RadioGroup.indexOfChild(mbtiBlock7Ans2RadioGroup.findViewById(mbtiBlock7Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock7Ans3RadioGroup.indexOfChild(mbtiBlock7Ans3RadioGroup.findViewById(mbtiBlock7Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock7Ans4RadioGroup.indexOfChild(mbtiBlock7Ans4RadioGroup.findViewById(mbtiBlock7Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock7Ans5RadioGroup.indexOfChild(mbtiBlock7Ans5RadioGroup.findViewById(mbtiBlock7Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock7Ans6RadioGroup.indexOfChild(mbtiBlock7Ans6RadioGroup.findViewById(mbtiBlock7Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock7Ans7RadioGroup.indexOfChild(mbtiBlock7Ans7RadioGroup.findViewById(mbtiBlock7Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock8Ans1RadioGroup.indexOfChild(mbtiBlock8Ans1RadioGroup.findViewById(mbtiBlock8Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock8Ans2RadioGroup.indexOfChild(mbtiBlock8Ans2RadioGroup.findViewById(mbtiBlock8Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock8Ans3RadioGroup.indexOfChild(mbtiBlock8Ans3RadioGroup.findViewById(mbtiBlock8Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock8Ans4RadioGroup.indexOfChild(mbtiBlock8Ans4RadioGroup.findViewById(mbtiBlock8Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock8Ans5RadioGroup.indexOfChild(mbtiBlock8Ans5RadioGroup.findViewById(mbtiBlock8Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock8Ans6RadioGroup.indexOfChild(mbtiBlock8Ans6RadioGroup.findViewById(mbtiBlock8Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock8Ans7RadioGroup.indexOfChild(mbtiBlock8Ans7RadioGroup.findViewById(mbtiBlock8Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock9Ans1RadioGroup.indexOfChild(mbtiBlock9Ans1RadioGroup.findViewById(mbtiBlock9Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock9Ans2RadioGroup.indexOfChild(mbtiBlock9Ans2RadioGroup.findViewById(mbtiBlock9Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock9Ans3RadioGroup.indexOfChild(mbtiBlock9Ans3RadioGroup.findViewById(mbtiBlock9Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock9Ans4RadioGroup.indexOfChild(mbtiBlock9Ans4RadioGroup.findViewById(mbtiBlock9Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock9Ans5RadioGroup.indexOfChild(mbtiBlock9Ans5RadioGroup.findViewById(mbtiBlock9Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock9Ans6RadioGroup.indexOfChild(mbtiBlock9Ans6RadioGroup.findViewById(mbtiBlock9Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock9Ans7RadioGroup.indexOfChild(mbtiBlock9Ans7RadioGroup.findViewById(mbtiBlock9Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                    MBTIQuestions(
                        mbtiBlock10Ans1RadioGroup.indexOfChild(mbtiBlock10Ans1RadioGroup.findViewById(mbtiBlock10Ans1RadioGroup.checkedRadioButtonId)),
                        mbtiBlock10Ans2RadioGroup.indexOfChild(mbtiBlock10Ans2RadioGroup.findViewById(mbtiBlock10Ans2RadioGroup.checkedRadioButtonId)),
                        mbtiBlock10Ans3RadioGroup.indexOfChild(mbtiBlock10Ans3RadioGroup.findViewById(mbtiBlock10Ans3RadioGroup.checkedRadioButtonId)),
                        mbtiBlock10Ans4RadioGroup.indexOfChild(mbtiBlock10Ans4RadioGroup.findViewById(mbtiBlock10Ans4RadioGroup.checkedRadioButtonId)),
                        mbtiBlock10Ans5RadioGroup.indexOfChild(mbtiBlock10Ans5RadioGroup.findViewById(mbtiBlock10Ans5RadioGroup.checkedRadioButtonId)),
                        mbtiBlock10Ans6RadioGroup.indexOfChild(mbtiBlock10Ans6RadioGroup.findViewById(mbtiBlock10Ans6RadioGroup.checkedRadioButtonId)),
                        mbtiBlock10Ans7RadioGroup.indexOfChild(mbtiBlock10Ans7RadioGroup.findViewById(mbtiBlock10Ans7RadioGroup.checkedRadioButtonId)),
                    ),
                )
            )

            MainRepository(ApiHelper(ApiServiceImpl())).sendMBTI(mbti, token.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(applicationContext, "Вы прошли тест", Toast.LENGTH_LONG).show()
                    finish()}, {
                        throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG).show() // TODO: Обработать все json теги
                    }
                })
        }
    }
}
