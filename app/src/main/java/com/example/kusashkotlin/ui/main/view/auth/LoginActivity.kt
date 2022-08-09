package com.example.kusashkotlin.ui.main.view.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.data.repo.PreferencesRepository
import com.example.kusashkotlin.ui.main.view.profile.UserProfileActivity
import com.example.kusashkotlin.ui.main.viewmodel.LoginTokenViewModel
import com.example.kusashkotlin.ui.main.viewmodel.LoginUserViewModel
import com.example.kusashkotlin.utils.Status
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_profile.progressBar


class LoginActivity : AppCompatActivity() {

    @BindView(R.id.loginButton)
    lateinit var loginButton: Button

    private lateinit var save: SharedPreferences

    private lateinit var token: String

    private lateinit var tokenViewModel: LoginTokenViewModel

    private lateinit var userViewModel: LoginUserViewModel

    lateinit var username: String

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // работу с sharedPreferences лучше вынести в репозиторий, а тут просто вызывать метод репозитория.
        // Тогда реализацию хранения можно будет менять не изменяя код activity
        save = getSharedPreferences("APP", MODE_PRIVATE)
        AndroidNetworking.initialize(getApplicationContext());
        token = save.getString("token", "").toString()
        // Если имя пользователя не пустое, значит токен валидный.
        username = save.getString("username", "").toString()

        if (PreferencesRepository().getAllRoles() == null) {
            MainRepository(ApiHelper(ApiServiceImpl())).getBelbilnRoles()
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                save.edit().putString("roles", Gson().toJson(response)).apply()
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show()
                }
            })
        }

        if (PreferencesRepository().getAllSpecializations() == null) {
            MainRepository(ApiHelper(ApiServiceImpl())).getSpecializations()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    save.edit().putString("specializations", Gson().toJson(response)).apply()
                }, { throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }

        // Валидный ли токен
        setupUserViewModel()
        setUpUserObserver()
        if (token == "" || username == "") { // если из репозитория у тебя будет приходить объект содержащий поля token/username то эту логику можно инкапсулировать в нем в методе isAuthorized()
            setContent()
        } else {
            //setPreferences(token, username)
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    // в репозиторий
    private fun setPreferences(token: String, username: String) {
        val edit: SharedPreferences.Editor = save.edit()
        edit.putString("username", username)
        edit.putString("token", token)
        edit.apply()
    }


    private fun setContent() {
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        loginButton.setOnClickListener {
            setupTokenViewModel()
            setTokenObserver()
        }
        registerClickableTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setTokenObserver() {
        tokenViewModel.getToken().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    loginButton.visibility = View.VISIBLE
                    token = it.data?.token ?: ""
                    setPreferences(token, loginEditText.text.toString())
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.LOADING -> {
                    loginButton.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loginButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).setMessage(it.message).create().show()
                }
            }
        })
    }

    private fun setUpUserObserver() {
        userViewModel.getUser().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    username = ""
                    setPreferences("", "")
                }
            }
        })
    }

    private fun setupTokenViewModel() {
        tokenViewModel = LoginTokenViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            loginEditText.text.toString(),
            passwordEditText.text.toString()
        )
    }

    // может быть инициализирован с пустым токено. Вряд ли это то что нам нужно
    private fun setupUserViewModel() {
        userViewModel = LoginUserViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            token
        )
    }
}
