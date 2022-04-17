package com.example.kusashkotlin.ui.main.view.profile

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.example.kusashkotlin.R
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import android.widget.TextView
import butterknife.ButterKnife
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.ui.base.ViewModelFactory


class UserProfileActivity : AppCompatActivity() {

    @BindView(R.id.nameTextView)
    lateinit var nameTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        ButterKnife.bind(this)

        val viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(ApiServiceImpl()))).get(ProfileViewModel::class.java, )

        viewModel.profile.observe(this, Observer {
            nameTextView.text = it.data?.desctiption ?: ""
            it.data?.let { it1 -> Log.d("mytag", it1.age) }
        })

    }
}