package com.example.kusashkotlin.ui.main.view.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.example.kusashkotlin.R
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.ui.base.ViewModelFactory
import com.example.kusashkotlin.utils.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.item_layout.surnameTextView


class UserProfileActivity : AppCompatActivity() {

    @BindView(R.id.nameTextView)
    lateinit var nameTextView: TextView

    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        ButterKnife.bind(this)
        setupViewModel()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getProfile().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    nameTextView.text = it.data?.user?.firstName ?: ""
                    surnameTextView.text = it.data?.user?.lastName ?: ""
                    Picasso.with(this).load(Uri.parse(it.data?.photo ?: "")).into(avatarImageView)

                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(ApiServiceImpl()),
            getPreferences(MODE_PRIVATE).getString("username", "").toString()))
            .get(ProfileViewModel::class.java
        )
    }


}