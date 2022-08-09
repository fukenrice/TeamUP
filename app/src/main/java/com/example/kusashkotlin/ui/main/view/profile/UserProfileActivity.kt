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
import butterknife.BindView
import butterknife.ButterKnife
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityUserProfileBinding
import com.example.kusashkotlin.ui.main.view.auth.LoginActivity
import com.example.kusashkotlin.ui.main.view.offers.ExecutorOfferEditActivity
import com.example.kusashkotlin.ui.main.view.offers.ExecutorOffersListActivity
import com.example.kusashkotlin.ui.main.view.project.CurrentProjectsActivity
import com.example.kusashkotlin.ui.main.view.project.EditProjectActivity
import com.example.kusashkotlin.ui.main.view.project.ProjectListActivity
import com.example.kusashkotlin.ui.main.view.slots.AppliedWorkerSLotsActivity
import com.example.kusashkotlin.ui.main.view.slots.ProjectWorkerSlotListActivity
import com.example.kusashkotlin.ui.main.view.slots.SentRequestsWorkersSlotsActivity
import com.example.kusashkotlin.ui.main.view.tests.BelbinActivity
import com.example.kusashkotlin.ui.main.view.tests.MBTIActivity
import com.example.kusashkotlin.ui.main.viewmodel.AppliedSlotsViewModel
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import com.example.kusashkotlin.utils.Status
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*

/// Не буду повторяться тут все тоже самое token, репоззиторий, константы, строки
class UserProfileActivity : AppCompatActivity() {

    @BindView(R.id.nameTextView)
    lateinit var nameTextView: TextView

    lateinit var viewModel: ProfileViewModel

    private lateinit var save: SharedPreferences

    private lateinit var mDrawer: Drawer

    private lateinit var binding: ActivityUserProfileBinding

    private var observerProfile: Profile? = null

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
                        lsqListView.setAdapter(
                            ArrayAdapter<String>(
                                this, android.R.layout.simple_list_item_1,
                                it.data.lsq!!
                            )
                        )
                    }
                    binding.profile = it.data

                    it.data?.let { it1 -> save.edit().putInt("id", it1.id) }

                    progressBar.visibility = View.GONE

                    if (it.data?.photo ?: "" == "") {
                        Picasso.with(this)
                            .load(Uri.parse("android.resource://${packageName}/${R.raw.empty}"))
                            .fit().centerCrop()
                            .into(avatarImageView)
                    } else {
                        Picasso.with(this).load(Uri.parse(it.data?.photo ?: "")).fit().centerCrop()
                            .into(avatarImageView)
                    }

                    if (it.data?.belbin != null) {
                        belbinListView.setAdapter(
                            ArrayAdapter<String>(
                                this, android.R.layout.simple_list_item_1,
                                it.data.belbin!!
                            )
                        )
                    }

                    if (it.data?.specialzation != null) {
                        profileViewSpecializationsListView.setAdapter(
                            ArrayAdapter<String>(
                                this, android.R.layout.simple_list_item_1,
                                it.data.specialzation!!
                            )
                        )
                    }

                    if (it.data?.mbti != null) {
                        mbtiListView.setAdapter(
                            ArrayAdapter<String>(
                                this, android.R.layout.simple_list_item_1,
                                it.data.mbti!!
                            )
                        )
                    }


                    if (it.data?.cv != null) {
                        cvTextView.setText("Скачать резюме")
                        cvTextView.setTextColor(Color.BLUE)
                        cvTextView.setOnClickListener {
                            // TODO: Сетевой запрос на загрузку резюме.
                        }
                    }
                    setSupportActionBar(toolbar)
                    setDrawer()
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
        if (intent.getStringExtra("username") == null) {
            viewModel = ProfileViewModel(
                MainRepository(ApiHelper(ApiServiceImpl())),
                save.getString("username", "").toString()
            )
        } else {
            viewModel = ProfileViewModel(
                MainRepository(ApiHelper(ApiServiceImpl())),
                intent.getStringExtra("username").toString()
            )
        }
    }

    private fun setContent() {
        setupViewModel()
        setupObserver()

        if (intent.getStringExtra("username") != null) {
            logoutButton.visibility = View.GONE
            profileInviteButton.visibility = View.VISIBLE
            viewModel.getOtherProfile(save.getString("username", "").toString())
            viewModel.getObserverProfile().observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        observerProfile = it.data
                    }
                }
            })
        }

        profileInviteButton.setOnClickListener {
            if (observerProfile?.project ?: "" != "") {
                val intent: Intent =
                    Intent(this, ProjectWorkerSlotListActivity::class.java)
                intent.putExtra(
                    "username", viewModel.getProfile().value?.data?.user?.username
                )
                intent.putExtra("project_title", observerProfile?.project)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Сначала создайте проект", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        logoutButton.setOnClickListener {
            save.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        button_toggle.setOnClickListener {
            button_toggle.setText(if (expandableTextView.isExpanded) R.string.expand else R.string.collapse)
            expandableTextView.toggle()
        }

    }

    private fun setDrawer() {

        if (intent.getStringExtra("username") == null) {
            mDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                    PrimaryDrawerItem().withIdentifier(1).withName("Редактировать профиль")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(2).withName("Пройти тест Белибна")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(3).withName("Пройти тест Майерса-Бриггса")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(4)
                        .withName("Управление предложением работника").withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(5).withName("Управление проектом")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(6).withName("Посмотреть проекты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(7).withName("Посмотреть работников")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(8).withName("Посмотреть приглашения")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(9).withName("Посмотреть свои проекты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(10)
                        .withName("Посмотреть неодобренные заявки")
                        .withSelectable(false),
                ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(
                        view: View?,
                        position: Int,
                        drawerItem: IDrawerItem<*>
                    ): Boolean {
                        if (position == 0) {
                            startActivity(Intent(applicationContext, EditActivity::class.java))
                        }
                        if (position == 1) {
                            startActivity(Intent(applicationContext, BelbinActivity::class.java))
                        }
                        if (position == 2) {
                            startActivity(Intent(applicationContext, MBTIActivity::class.java))
                        }
                        if (position == 3) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    ExecutorOfferEditActivity::class.java
                                )
                            )
                        }
                        if (position == 4) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    EditProjectActivity::class.java
                                )
                            )
                        }
                        if (position == 5) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    ProjectListActivity::class.java
                                )
                            )
                        }
                        if (position == 6) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    ExecutorOffersListActivity::class.java
                                )
                            )
                        }
                        if (position == 7) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    AppliedWorkerSLotsActivity::class.java
                                )
                            )
                        }
                        if (position == 8) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    CurrentProjectsActivity::class.java
                                )
                            )
                        }
                        if (position == 9) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    SentRequestsWorkersSlotsActivity::class.java
                                )
                            )
                        }
                        return true
                    }
                })
                .build()
        }
    }
}
