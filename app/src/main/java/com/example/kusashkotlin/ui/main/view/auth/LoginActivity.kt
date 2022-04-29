package com.example.kusashkotlin.ui.main.view.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import com.androidnetworking.AndroidNetworking
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.view.profile.UserProfileActivity
import com.example.kusashkotlin.ui.main.viewmodel.LoginTokenViewModel
import com.example.kusashkotlin.ui.main.viewmodel.LoginUserViewModel
import com.example.kusashkotlin.utils.Status
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        AndroidNetworking.initialize(getApplicationContext());
        token = save.getString("token", "").toString()
        // Если имя пользователя не пустое, значит токен валидный.
        username = save.getString("username", "").toString()
        // Валидный ли токен
        setupUserViewModel()
        setUpUserObserver()
        if (token == "" || username == "") {
            setContent()
        } else {
            //setPreferences(token, username)
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


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
                    Log.d("mytag", it.data?.token ?: "")
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

    private fun setupUserViewModel() {
        userViewModel = LoginUserViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            token
        )
    }
}
