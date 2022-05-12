package com.example.kusashkotlin.ui.main.view.tests

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
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
                            block1Ans1NumericInput.text.toString().toIntOrZero(),
                            block1Ans2NumericInput.text.toString().toIntOrZero(),
                            block1Ans3NumericInput.text.toString().toIntOrZero(),
                            block1Ans4NumericInput.text.toString().toIntOrZero(),
                            block1Ans5NumericInput.text.toString().toIntOrZero(),
                            block1Ans6NumericInput.text.toString().toIntOrZero(),
                            block1Ans7NumericInput.text.toString().toIntOrZero(),
                            block1Ans8NumericInput.text.toString().toIntOrZero(),
                        ),
                        BelbinQuestions(
                            block2Ans1NumericInput.text.toString().toIntOrZero(),
                            block2Ans2NumericInput.text.toString().toIntOrZero(),
                            block2Ans3NumericInput.text.toString().toIntOrZero(),
                            block2Ans4NumericInput.text.toString().toIntOrZero(),
                            block2Ans5NumericInput.text.toString().toIntOrZero(),
                            block2Ans6NumericInput.text.toString().toIntOrZero(),
                            block2Ans7NumericInput.text.toString().toIntOrZero(),
                            block2Ans8NumericInput.text.toString().toIntOrZero(),
                        ),
                        BelbinQuestions(
                            block3Ans1NumericInput.text.toString().toIntOrZero(),
                            block3Ans2NumericInput.text.toString().toIntOrZero(),
                            block3Ans3NumericInput.text.toString().toIntOrZero(),
                            block3Ans4NumericInput.text.toString().toIntOrZero(),
                            block3Ans5NumericInput.text.toString().toIntOrZero(),
                            block3Ans6NumericInput.text.toString().toIntOrZero(),
                            block3Ans7NumericInput.text.toString().toIntOrZero(),
                            block3Ans8NumericInput.text.toString().toIntOrZero(),
                        ),
                        BelbinQuestions(
                            block4Ans1NumericInput.text.toString().toIntOrZero(),
                            block4Ans2NumericInput.text.toString().toIntOrZero(),
                            block4Ans3NumericInput.text.toString().toIntOrZero(),
                            block4Ans4NumericInput.text.toString().toIntOrZero(),
                            block4Ans5NumericInput.text.toString().toIntOrZero(),
                            block4Ans6NumericInput.text.toString().toIntOrZero(),
                            block4Ans7NumericInput.text.toString().toIntOrZero(),
                            block4Ans8NumericInput.text.toString().toIntOrZero(),
                        ),
                        BelbinQuestions(
                            block5Ans1NumericInput.text.toString().toIntOrZero(),
                            block5Ans2NumericInput.text.toString().toIntOrZero(),
                            block5Ans3NumericInput.text.toString().toIntOrZero(),
                            block5Ans4NumericInput.text.toString().toIntOrZero(),
                            block5Ans5NumericInput.text.toString().toIntOrZero(),
                            block5Ans6NumericInput.text.toString().toIntOrZero(),
                            block5Ans7NumericInput.text.toString().toIntOrZero(),
                            block5Ans8NumericInput.text.toString().toIntOrZero(),
                        ),
                        BelbinQuestions(
                            block6Ans1NumericInput.text.toString().toIntOrZero(),
                            block6Ans2NumericInput.text.toString().toIntOrZero(),
                            block6Ans3NumericInput.text.toString().toIntOrZero(),
                            block6Ans4NumericInput.text.toString().toIntOrZero(),
                            block6Ans5NumericInput.text.toString().toIntOrZero(),
                            block6Ans6NumericInput.text.toString().toIntOrZero(),
                            block6Ans7NumericInput.text.toString().toIntOrZero(),
                            block6Ans8NumericInput.text.toString().toIntOrZero(),
                        ),
                        BelbinQuestions(
                            block7Ans1NumericInput.text.toString().toIntOrZero(),
                            block7Ans2NumericInput.text.toString().toIntOrZero(),
                            block7Ans3NumericInput.text.toString().toIntOrZero(),
                            block7Ans4NumericInput.text.toString().toIntOrZero(),
                            block7Ans5NumericInput.text.toString().toIntOrZero(),
                            block7Ans6NumericInput.text.toString().toIntOrZero(),
                            block7Ans7NumericInput.text.toString().toIntOrZero(),
                            block7Ans8NumericInput.text.toString().toIntOrZero(),
                        )
                    )
                )
            sendBelbin()
        }
    }

    @SuppressLint("CheckResult")
    private fun sendBelbin() {
        MainRepository(ApiHelper(ApiServiceImpl())).sendBelbin(belbin, token.toString())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({response -> Toast.makeText(applicationContext, "Вы прошли тест", Toast.LENGTH_LONG).show()
                       finish()}, {
                    throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG).show() // TODO: Обработать все json теги
                }
            })
    }
}

private fun String.toIntOrZero(): Int {
    return toIntOrNull() ?: return 0
}
