package com.example.kusashkotlin.ui.main.view.tests

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.BelbinModel
import com.example.kusashkotlin.data.model.BelbinQuestions
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.InputFilterMinMax
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_belbin.*


class BelbinActivity : AppCompatActivity() {

    lateinit var belbin: BelbinModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        setContentView(R.layout.activity_belbin)
        setContent()
    }

    private fun setContent() {
        val layout = findViewById<ConstraintLayout>(R.id.belbinLayout)
        for (view in layout.children) {
            if (view is EditText) {
                view.filters = arrayOf<InputFilter>(InputFilterMinMax("0", "10"))
            }
        }



        confirmBelbinButton.setOnClickListener {
            belbin =
                BelbinModel(
                    listOf(
                        BelbinQuestions(
                            block1Ans1NumericInput.text.toString().toInt(),
                            block1Ans2NumericInput.text.toString().toInt(),
                            block1Ans3NumericInput.text.toString().toInt(),
                            block1Ans4NumericInput.text.toString().toInt(),
                            block1Ans5NumericInput.text.toString().toInt(),
                            block1Ans6NumericInput.text.toString().toInt(),
                            block1Ans7NumericInput.text.toString().toInt(),
                            block1Ans8NumericInput.text.toString().toInt(),
                        ),
                        BelbinQuestions(
                            block2Ans1NumericInput.text.toString().toInt(),
                            block2Ans2NumericInput.text.toString().toInt(),
                            block2Ans3NumericInput.text.toString().toInt(),
                            block2Ans4NumericInput.text.toString().toInt(),
                            block2Ans5NumericInput.text.toString().toInt(),
                            block2Ans6NumericInput.text.toString().toInt(),
                            block2Ans7NumericInput.text.toString().toInt(),
                            block2Ans8NumericInput.text.toString().toInt(),
                        ),
                        BelbinQuestions(
                            block3Ans1NumericInput.text.toString().toInt(),
                            block3Ans2NumericInput.text.toString().toInt(),
                            block3Ans3NumericInput.text.toString().toInt(),
                            block3Ans4NumericInput.text.toString().toInt(),
                            block3Ans5NumericInput.text.toString().toInt(),
                            block3Ans6NumericInput.text.toString().toInt(),
                            block3Ans7NumericInput.text.toString().toInt(),
                            block3Ans8NumericInput.text.toString().toInt(),
                        ),
                        BelbinQuestions(
                            block4Ans1NumericInput.text.toString().toInt(),
                            block4Ans2NumericInput.text.toString().toInt(),
                            block4Ans3NumericInput.text.toString().toInt(),
                            block4Ans4NumericInput.text.toString().toInt(),
                            block4Ans5NumericInput.text.toString().toInt(),
                            block4Ans6NumericInput.text.toString().toInt(),
                            block4Ans7NumericInput.text.toString().toInt(),
                            block4Ans8NumericInput.text.toString().toInt(),
                        ),
                        BelbinQuestions(
                            block5Ans1NumericInput.text.toString().toInt(),
                            block5Ans2NumericInput.text.toString().toInt(),
                            block5Ans3NumericInput.text.toString().toInt(),
                            block5Ans4NumericInput.text.toString().toInt(),
                            block5Ans5NumericInput.text.toString().toInt(),
                            block5Ans6NumericInput.text.toString().toInt(),
                            block5Ans7NumericInput.text.toString().toInt(),
                            block5Ans8NumericInput.text.toString().toInt(),
                        ),
                        BelbinQuestions(
                            block6Ans1NumericInput.text.toString().toInt(),
                            block6Ans2NumericInput.text.toString().toInt(),
                            block6Ans3NumericInput.text.toString().toInt(),
                            block6Ans4NumericInput.text.toString().toInt(),
                            block6Ans5NumericInput.text.toString().toInt(),
                            block6Ans6NumericInput.text.toString().toInt(),
                            block6Ans7NumericInput.text.toString().toInt(),
                            block6Ans8NumericInput.text.toString().toInt(),
                        ),
                        BelbinQuestions(
                            block7Ans1NumericInput.text.toString().toInt(),
                            block7Ans2NumericInput.text.toString().toInt(),
                            block7Ans3NumericInput.text.toString().toInt(),
                            block7Ans4NumericInput.text.toString().toInt(),
                            block7Ans5NumericInput.text.toString().toInt(),
                            block7Ans6NumericInput.text.toString().toInt(),
                            block7Ans7NumericInput.text.toString().toInt(),
                            block7Ans8NumericInput.text.toString().toInt(),
                        )
                    )
                )
            Log.d("belbin points", belbin.value.get(6).answer0.toString())
            sendBelbin()
        }
    }

    @SuppressLint("CheckResult")
    private fun sendBelbin() {
        MainRepository(ApiHelper(ApiServiceImpl())).sendBelbin(belbin, token.toString())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({response -> Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG).show()
                       finish()}, {
                    throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG).show() // Обработать все json теги
                }
            })
    }
}