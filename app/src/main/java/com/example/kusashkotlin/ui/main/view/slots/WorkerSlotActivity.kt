package com.example.kusashkotlin.ui.main.view.slots

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.example.kusashkotlin.databinding.ActivityProjectBinding
import com.example.kusashkotlin.databinding.ActivityWorkerSlotBinding
import com.example.kusashkotlin.ui.main.viewmodel.ProjectViewModel
import com.example.kusashkotlin.ui.main.viewmodel.WorkerSlotViewModel
import com.example.kusashkotlin.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_project.*
import kotlinx.android.synthetic.main.activity_worker_slot.*
import kotlin.properties.Delegates

class WorkerSlotActivity : AppCompatActivity() {

    lateinit var allSpecializations: List<SpecializationModel>

    lateinit var allBelbinRoles: List<RoleModel>

    lateinit var viewModel: WorkerSlotViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    private lateinit var binding: ActivityWorkerSlotBinding

    private var id by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_slot)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_worker_slot)
        id = intent.getIntExtra("id", -1)
        getBelbinRoles()
        getSpecializations()
    }

    fun setContent() {
        setupViewModel()
        setupObserver()
        workerSlotRequestButton.setOnClickListener {
            viewModel.applyToWorkerSlot(token.toString())
        }
        workerSlotLeaveButton.setOnClickListener {
            viewModel.leaveWorkerSlot(token.toString())
            finish()
        }
    }

    fun setupViewModel() {
        viewModel =
            WorkerSlotViewModel(MainRepository(ApiHelper(ApiServiceImpl())), id, context = this)
    }

    fun setupObserver() {
        viewModel.getWorkerSlot().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.slot = it.data

                    workerSlotRolesListView.adapter

                    val specializations: MutableList<String> =
                        it.data?.specializations?.let { it1 ->
                            getSpecializationsListByIndex(
                                it1
                            )
                        }!!

                    workerSlotRolesListView.adapter = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        specializations
                    )

                    if (it.data.profile == save.getString("username", "")) {
                        workerSlotRequestButton.visibility = View.GONE
                        workerSlotLeaveButton.visibility = View.VISIBLE
                    } else {
                        workerSlotRequestButton.visibility = View.VISIBLE
                        workerSlotLeaveButton.visibility = View.GONE
                    }

                    val belbinRoles: MutableList<String> = it.data.roles.let { it1 ->
                        getBelbinListByIndex(
                            it1
                        )
                    }

                    workerSlotRolesListView.adapter =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, belbinRoles)
                }
            }
        })
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
                setContent()
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show() // TODO: Обработать все json теги
                }
            })
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
}