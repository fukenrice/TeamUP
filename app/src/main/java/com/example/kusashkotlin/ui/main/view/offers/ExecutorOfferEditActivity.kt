package com.example.kusashkotlin.ui.main.view.offers

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ExecutorOfferSetupModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityExecutorOfferBinding
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import com.example.kusashkotlin.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_executor_offer.*

class ExecutorOfferEditActivity : AppCompatActivity() {


    private lateinit var save: SharedPreferences

    private var token: String? = null

    private lateinit var offer: ExecutorOfferSetupModel

    private lateinit var viewModel: ProfileViewModel

    private lateinit var binding: ActivityExecutorOfferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_executor_offer)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        setContent()
    }

    @SuppressLint("CheckResult")
    private fun setContent() {

        viewModel = ProfileViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            save.getString("username", "").toString()
        )

        setupProfileObserver()

        executorOfferConfimButton.setOnClickListener {
            if (executorOfferDescriptionTextEdit.text.isEmpty()) {
                Toast.makeText(applicationContext, "Пожалуйста, заполните описание", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (executorOfferWorkingHoursTextEdit.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Пожалуйста, заполните желаемое количество часов",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            offer = ExecutorOfferSetupModel(
                executorOfferDescriptionTextEdit.text.toString(),
                exectorOfferSalaryTextEdit.text.toString().toInt(),
                executorOfferWorkingHoursTextEdit.text.toString().toInt()
            )

            MainRepository(ApiHelper(ApiServiceImpl())).updateExecutorOffer(offer, token.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(applicationContext, "Предложение работника успешно отредактированно", Toast.LENGTH_LONG).show()
                    finish()}, {
                        throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG).show() // TODO: Обработать все json теги
                    }
                })
        }

        executorOfferDeleteButton.setOnClickListener {
            if (viewModel.getProfile().value?.data?.executorOffer == null) {
                Toast.makeText(applicationContext, "У вас нет активного предложения работника", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            MainRepository(ApiHelper(ApiServiceImpl())).deleteExecutorOffer(token.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(applicationContext, "Вы удалили свое предложение работника", Toast.LENGTH_LONG).show()
                    finish()}, {
                        throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG).show() // TODO: Обработать все json теги
                    }
                })



        }


    }

    private fun setupProfileObserver() {
        viewModel.getProfile().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.offer = it.data?.executorOffer
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

            }
        })
    }


}