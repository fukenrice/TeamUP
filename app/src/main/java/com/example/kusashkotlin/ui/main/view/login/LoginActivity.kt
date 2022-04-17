package com.example.kusashkotlin.ui.main.view.login

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
import com.example.kusashkotlin.ui.main.view.profile.UserProfileActivity
import com.google.android.material.textfield.TextInputEditText
import java.net.InetAddress


class LoginActivity : AppCompatActivity() {

    @BindView(R.id.loginButton)
    public lateinit var loginButton: Button

    @BindView(R.id.loginEditText)
    public lateinit var loginTextEdit: TextInputEditText

    @BindView(R.id.passwordEditText)
    public lateinit var passwordTextEdit: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidNetworking.initialize(getApplicationContext());

        val save: SharedPreferences = getPreferences(MODE_PRIVATE)
        val token = save.getString("token", "")

        if (token == "") {
            // Отображаем текущую страницу
        } else {
            // Делаем зарпос на получение профиля, если пришел с ошибкой, значит токен невалидный и отображаем текущую
        }


        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        // Проверяем, есть ли логин в бд на сервере, если есть, то запускаем профиль, если нет, то остаемся в текущей активити.

//        loginButton.setOnClickListener {
//            // Запрос к серверу на поодтверждение данных
//        }


        val gfgThread = Thread {
            try {
                Log.d("mytag", isInternetAvailable().toString())
            } catch (e: java.lang.Exception) {
                throw e
            }
        }

        gfgThread.start()

        loginButton.setOnClickListener {
            val intent: Intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

    }

    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            Log.d("mytag", ipAddr.hostName)
            !ipAddr.equals("")

        } catch (e: Exception) {
            throw e
            //false
        }
    }

    override fun onPause() {
        super.onPause()
        val save: SharedPreferences = getPreferences(MODE_PRIVATE)
        val edit: SharedPreferences.Editor = save.edit()
        edit.putString("username", "fukenrice")
        edit.apply()
    }
}
