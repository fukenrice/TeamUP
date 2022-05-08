package com.example.kusashkotlin.ui.main.view.profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProfileUpdate
import com.example.kusashkotlin.data.model.User
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityEditBinding
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import com.example.kusashkotlin.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    lateinit var viewModel: ProfileViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        setContent()
        // TODO: Сделать изменение пароля
    }


    private fun setupProfileObserver() {
        viewModel.getProfile().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    binding.profile = it.data

                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    @SuppressLint("CheckResult")
    private fun setContent() {
        setupViewModel()
        setupProfileObserver()
        buttonEditConfirm.setOnClickListener {
            viewModel.getProfile().value?.data?.user?.let { it1 ->
                Log.d(
                    "editname",
                    it1.firstName
                )
            }

            var remote: Int? = null

            if (remoteRadioGroup.indexOfChild(remoteRadioGroup.findViewById(remoteRadioGroup.checkedRadioButtonId)) != -1) {
                remote =
                    remoteRadioGroup.indexOfChild(remoteRadioGroup.findViewById(remoteRadioGroup.checkedRadioButtonId)) + 1
            }

            var isMale: Boolean? = null

            if (sexRadioGroup.indexOfChild(sexRadioGroup.findViewById(sexRadioGroup.checkedRadioButtonId)) != -1) {
                isMale =
                    sexRadioGroup.indexOfChild(sexRadioGroup.findViewById(sexRadioGroup.checkedRadioButtonId)) == 0
            }

            val update = ProfileUpdate(
                remote = remote,
                isMale = isMale,
                patronymic = patronymicEditTextEdit.text.toString(),
                user = User(
                    firstName = editNameEditText.text.toString(),
                    lastName = lastnameEditEditText.text.toString(),
                    email = emailEditTextEdit.text.toString()
                ),
                city = cityEditTextEdit.text.toString(),
                age = ageEditTextEdit.text.toString().toInt(),
                description = descriptionEditTextEdit.text.toString()
            )


            MainRepository(ApiHelper(ApiServiceImpl())).editProfile(update, token.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG)
                        .show()
                    finish()
                }, { throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                            .show() // Обработать все json теги
                    }
                })
        }

        radioEmpty.setOnClickListener(onRadioButtonClicked())
        radioFemale.setOnClickListener(onRadioButtonClicked())
        radioMale.setOnClickListener(onRadioButtonClicked())
        radioNo.setOnClickListener(onRadioButtonClicked())
        radioYes.setOnClickListener(onRadioButtonClicked())
    }

    private fun onRadioButtonClicked(): View.OnClickListener? {
        return View.OnClickListener { }
    }

    private fun setupViewModel() {
        viewModel = ProfileViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            save.getString("username", "").toString()
        )
    }
}
