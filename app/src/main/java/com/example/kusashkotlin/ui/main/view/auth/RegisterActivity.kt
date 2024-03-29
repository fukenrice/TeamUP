package com.example.kusashkotlin.ui.main.view.auth

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.viewmodel.RegisterViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    lateinit var viewModel: RegisterViewModel

    private lateinit var save: SharedPreferences

    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()

    }


    private fun setContent() {
        setContentView(R.layout.activity_register)
        ButterKnife.bind(this)

        buttonRegister.setOnClickListener {
            if (editTextTextUsername.text.isNullOrEmpty() ||
                editTextTextEmailAddress.text.isNullOrEmpty() ||
                editTextTextRepeatPassword.text.isNullOrEmpty() ||
                editTextTextPassword.text.isNullOrEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_LONG).show() // strings.xml
                return@setOnClickListener
            }
            if (editTextTextPassword.text.toString() != editTextTextRepeatPassword.text.toString()) {
                Toast.makeText(this, "Пароли должны совпадать", Toast.LENGTH_LONG).show() // strings.xml
            } else {
                setupViewModel()
                setUpObserver()
            }
        }
    }

    private fun setUpObserver() {
        viewModel.registerUser().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Вы успешно зарегистированы!", Toast.LENGTH_LONG).show()  // strings.xml
                    // startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).setMessage(it.message).create().show()
                }
            }
        })
    }

    // ViewModel вообще лучше создавать сразу при создании экрана.
    // Репозиторий можно инициализировать в конструкторе,  а параметры пердавать через функцию registerUser()
    // подписать экран сразу на стейт LiveData и рендерит UI в соответсвии со стейтом.
    private fun setupViewModel() {
        viewModel = RegisterViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            editTextTextEmailAddress.text.toString(),
            editTextTextUsername.text.toString(),
            editTextTextPassword.text.toString()
        )
    }
}
