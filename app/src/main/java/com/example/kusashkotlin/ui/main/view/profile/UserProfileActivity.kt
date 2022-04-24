package com.example.kusashkotlin.ui.main.view.profile

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.ui.base.ViewModelFactory
import com.example.kusashkotlin.ui.main.view.login.LoginActivity
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import com.example.kusashkotlin.utils.Status
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.progressBar
import kotlinx.android.synthetic.main.item_layout.surnameTextView


class UserProfileActivity : AppCompatActivity() {

    @BindView(R.id.nameTextView)
    lateinit var nameTextView: TextView

    lateinit var viewModel: ProfileViewModel

    private lateinit var save: SharedPreferences

    private lateinit var mDrawer: Drawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        ButterKnife.bind(this)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        setContent()
    }

    private fun setupObserver() {
        viewModel.getProfile().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    nameTextView.text = it.data?.user?.firstName ?: ""
                    surnameTextView.text = it.data?.user?.lastName ?: ""
                    Picasso.with(this).load(Uri.parse(it.data?.photo ?: "")).fit().centerCrop()
                        .into(avatarImageView)
                    if (it.data?.belbin != null) {
                        belbinListView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            it.data.belbin!!
                        ))
                    }

                    if (it.data?.mbti != null) {
                        mbtiListView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            it.data.mbti!!
                        ))
                    }

                    if (it.data?.lsq != null) {
                        lsqListView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            it.data.lsq!!
                        ))
                    }

                    if (it.data?.cv != null) {
                        cvTextView.setText("Скачать резюме")
                        cvTextView.setTextColor(Color.BLUE)
                        cvTextView.setOnClickListener {
                            // Сетевой запрос на загрузку резюме.
                        }
                    }

                    cityTextView.text = "Город: " + it.data?.city
                    ageTextView.text = "Возраст: ${it.data?.age ?: ""}"
                    sexTextView.text = "Пол: ${it.data?.sex}"

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
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelper(ApiServiceImpl()),
                save.getString("username", "").toString()
            )
        )
            .get(ProfileViewModel::class.java)
    }

    private fun setContent() {
        setupViewModel()
        setupObserver()

        logoutButton.setOnClickListener {
            save.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        button_toggle.setOnClickListener {
            button_toggle.setText(if (expandableTextView.isExpanded) R.string.expand else R.string.collapse)
            expandableTextView.toggle()
        }

        setSupportActionBar(toolbar)
        setDrawer()
    }

    private fun setDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(1).withName("Редактировать профиль").withSelectable(false)
            ).build()
    }

}
