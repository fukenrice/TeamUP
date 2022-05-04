package com.example.kusashkotlin.ui.main.view.project

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityEditProjectBinding
import com.example.kusashkotlin.ui.main.viewmodel.ProjectViewModel
import com.example.kusashkotlin.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_project.*

class ProjectActivity : AppCompatActivity() {

    lateinit var viewModel: ProjectViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    private lateinit var binding: ActivityEditProjectBinding

    lateinit var allSpecializations: List<SpecializationModel>

    lateinit var allBelbinRoles: List<RoleModel>

    lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_project)
        getBelbinRoles()
        getSpecializations()
        setContent()
    }

    fun setContent() {
        title = intent.getStringExtra("title").toString()
        setupViewModel()
        setupObserver()
    }

    fun getBelbinListByIndex(belbinIndexes: List<Int>): MutableList<String> {
        var belbinRoles = MutableList<String>(0) { i -> "" }
        var belbinCnt = belbinIndexes.size
        for (i in 0 until belbinCnt) {
            for (j in allBelbinRoles.indices) {
                if (allBelbinRoles[j].id == belbinIndexes.get(i)) {
                    belbinRoles.add(allBelbinRoles[j].role)
                }
            }
        }
        return belbinRoles
    }

    fun getSpecializationsListByIndex(specializationIndexes: List<Int>): MutableList<String> {
        var specializations = MutableList<String>(0) { i -> "" }
        var specializationCnt = specializationIndexes.size
        for (i in 0 until specializationCnt) {
            for (j in allSpecializations.indices) {
                if (allSpecializations[j].id == specializationIndexes.get(i)) {
                    specializations.add(allSpecializations[j].name)
                }
            }
        }
        return specializations
    }

    @SuppressLint("CheckResult")
    fun getBelbinRoles() {
        MainRepository(ApiHelper(ApiServiceImpl())).getBelbilnRoles()
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                allBelbinRoles = response
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show() // TODO: Обработать все json теги
                }
            })
    }

    @SuppressLint("CheckResult")
    fun getSpecializations() {
        MainRepository(ApiHelper(ApiServiceImpl())).getSpecializations()
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                allSpecializations = response
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show() // TODO: Обработать все json теги
                }
            })
    }


    fun setupObserver() {
        viewModel.getProject().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.project = it.data

                    val specializations: MutableList<String> =
                        it.data?.requiredSpecialization?.let { it1 ->
                            getSpecializationsListByIndex(
                                it1
                            )
                        }!!

                    projectEditSpecializationsListView.adapter = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        specializations
                    )

                    val belbinRoles: MutableList<String> = it.data.requiredBelbin.let { it1 ->
                        getBelbinListByIndex(
                            it1
                        )
                    }

                    projectEditBelbinListView.adapter =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, belbinRoles)
                }
            }
        })
    }


    fun setupViewModel() {
        viewModel = ProjectViewModel(MainRepository(ApiHelper(ApiServiceImpl())), title)
    }
}