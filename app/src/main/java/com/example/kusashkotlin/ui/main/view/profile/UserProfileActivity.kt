package com.example.kusashkotlin.ui.main.view.profile

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityUserProfileBinding
import com.example.kusashkotlin.ui.base.ViewModelFactory
import com.example.kusashkotlin.ui.main.view.login.LoginActivity
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import com.example.kusashkotlin.utils.Status
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.json.JSONObject
import java.io.File


class UserProfileActivity : AppCompatActivity() {

    @BindView(R.id.nameTextView)
    lateinit var nameTextView: TextView

    lateinit var viewModel: ProfileViewModel

    private lateinit var save: SharedPreferences

    private lateinit var mDrawer: Drawer

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        ButterKnife.bind(this)
        save = getSharedPreferences("APP", MODE_PRIVATE)


    }

    override fun onResume() {
        super.onResume()
        setContent()
    }

    private fun setupObserver() {
        viewModel.getProfile().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    if (it.data?.lsq != null) {
                        lsqListView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            it.data.lsq!!
                        ))
                    }
                    binding.profile = it.data

                    binding.profile?.user?.let { it1 -> Log.d("binding", it1.firstName) }

                    progressBar.visibility = View.GONE
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


                    if (it.data?.cv != null) {
                        cvTextView.setText("Скачать резюме")
                        cvTextView.setTextColor(Color.BLUE)
                        cvTextView.setOnClickListener {
                            // Сетевой запрос на загрузку резюме.
                        }
                    }
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
        viewModel = ProfileViewModel(MainRepository(ApiHelper(ApiServiceImpl())), save.getString("username", "").toString())
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
            ).withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    if (position == 0) {
                        startActivity(Intent(baseContext, EditActivity::class.java))
                    }
                    return true
                }
            })
            .build()
    }
}
