package com.example.kusashkotlin.ui.main.view.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.androidnetworking.AndroidNetworking
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.view.profile.UserProfileActivity
import com.google.android.material.textfield.TextInputEditText


class LoginActivity : AppCompatActivity() {

    @BindView(R.id.loginButton)
    lateinit var loginButton: Button

    @BindView(R.id.loginEditText)
    lateinit var loginTextEdit: TextInputEditText

    @BindView(R.id.passwordEditText)
    lateinit var passwordTextEdit: EditText

    lateinit var save: SharedPreferences

    //var context: Context = this

    var repository = MainRepository(ApiHelper(ApiServiceImpl()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getPreferences(MODE_PRIVATE)
        AndroidNetworking.initialize(getApplicationContext());

        save.edit().remove("username").commit()
        val token: String = save.getString("token", "").toString()
        // Если имя пользователя не пустое, значит токен валидный.
        val username = repository.getUsernameByToken(token)

        // Валидный ли токен
        if (token == "" || username == "") {
            setContent()
        } else {
            setPreferences(token, username)
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }


    }


    fun setPreferences(token: String, username: String) {
        val edit: SharedPreferences.Editor = save.edit()
        edit.putString("username", username)
        edit.putString("token", token)
        edit.apply()
    }

    fun setContent() {
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        ButterKnife.bind(this)
        loginButton.setOnClickListener {
            Log.d("login token", repository.getToken(loginTextEdit.text.toString(), passwordTextEdit.text.toString()))

            //val intent: Intent = Intent(this, UserProfileActivity::class.java)
            //startActivity(intent)
        }
    }

}
