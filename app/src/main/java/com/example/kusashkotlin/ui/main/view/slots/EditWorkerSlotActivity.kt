package com.example.kusashkotlin.ui.main.view.slots

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel
import com.example.kusashkotlin.data.model.WorkerSlot
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityEditWorkerSlotBinding
import com.example.kusashkotlin.ui.main.adapter.ProfileAdapter
import com.example.kusashkotlin.ui.main.view.profile.UserProfileActivity
import com.example.kusashkotlin.ui.main.viewmodel.WorkerSlotViewModel
import com.example.kusashkotlin.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_worker_slot.*

class EditWorkerSlotActivity : AppCompatActivity() {

    lateinit var allBelbinRoles: List<RoleModel>

    private lateinit var selectedRoles: BooleanArray

    private lateinit var selectedRolesIds: List<Int>

    lateinit var allSpecializations: List<SpecializationModel>

    lateinit var selectedSpecializations: BooleanArray

    lateinit var selectedSpecializationsIds: List<Int>

    lateinit var viewModel: WorkerSlotViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    private lateinit var binding: ActivityEditWorkerSlotBinding

    private var mode: String? = null

    private lateinit var adapter: ProfileAdapter

    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_worker_slot)
        mode = intent.getStringExtra("mode")
    }

    override fun onResume() {
        super.onResume()
        getBelbinRoles()
        getSpecializations()
        setContent()
    }

    @SuppressLint("CheckResult")
    fun setContent() {

        if (mode == "create") {
            workerSlotEditPretendingUsersTextView.visibility = View.GONE
            workerSlotEditPretendingUesrsRecyclerView.visibility = View.GONE
            workerSlotEditDeleteButton.visibility = View.GONE
            setupViewModel()
        } else {
            workerSlotEditPretendingUesrsRecyclerView.layoutManager = LinearLayoutManager(this)
            id = intent.getIntExtra("id", -1)
            adapter = ProfileAdapter({ username, slotId -> applyUser(username, slotId) },
                { username, slotId -> declineUser(username, slotId) },
                { position -> onListItemClick(position) }, mutableListOf(), id)
            workerSlotEditPretendingUesrsRecyclerView.adapter = adapter
            setupViewModel()
            setupAppliesObserver()

            workerSlotEditEmployeeTextView.setOnClickListener {
                val intent: Intent = Intent(this, UserProfileActivity::class.java)
                intent.putExtra("username",
                    viewModel.getWorkerSlot().value?.data?.profile
                )
                startActivity(intent)
            }
        }

        workerSlotEditChangeRolesButton.setOnClickListener {

            showChangeBelbinDialog()
        }
        workerSlotEditChangeSpecializationsButton.setOnClickListener {
            showChangeSpecializationsDialog()
        }
        workerSlotEditConfirmButton.setOnClickListener {
            var message: String = ""

            if (workerSlotEditDescriptionTextEdit.text.isEmpty()) {
                message += "Пожалуйста, введите описание вакансии\n"
            }
            if (workerSlotEditWorkingHoursTextEdit.text.isEmpty()) {
                message += "Пожалуйста, количество рабочих часов\n"
            }
            if (workerSlotEditRolesListView.isEmpty()) {
                message += "Пожалуйста, заполните роли по Белбину\n"
            }
            if (workerSlotEditSpecializationsListView.isEmpty()) {
                message += "Пожалуйста, заполните теребуемые специализации\n"
            }

            if (message != "") {
                AlertDialog.Builder(this).setMessage(message).create().show()
                return@setOnClickListener
            }
            val slot: WorkerSlot
            if (mode == "create") {
                slot = WorkerSlot(
                    id = null,
                    description = workerSlotEditDescriptionTextEdit.text.toString(),
                    salary = workerSlotEditSalaryTextEdit.text.toString().toIntOrNull(),
                    workingHours = workerSlotEditWorkingHoursTextEdit.text.toString().toIntOrNull(),
                    roles = selectedRolesIds,
                    specializations = selectedSpecializationsIds
                )
            } else {
                slot = viewModel.getWorkerSlot().value?.data!!
                id = slot.id!!
                slot.description = workerSlotEditDescriptionTextEdit.text.toString()
                slot.salary = workerSlotEditSalaryTextEdit.text.toString().toIntOrNull()
                slot.workingHours = workerSlotEditWorkingHoursTextEdit.text.toString().toIntOrNull()
                slot.roles = selectedRolesIds
                slot.specializations = selectedSpecializationsIds
            }

            MainRepository(ApiHelper(ApiServiceImpl())).updateWorkerSlot(token.toString(), slot)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
                    finish()
                }, { throwable ->
                    if (throwable is ANError) {
                        AlertDialog.Builder(this).setMessage(throwable.errorBody).show()
                    }
                })
        }

        workerSlotEditDeleteButton.setOnClickListener {
            val id: Int = viewModel.getWorkerSlot().value?.data?.id!!
            MainRepository(ApiHelper(ApiServiceImpl())).deleteWorkerSlot(id, token.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
                    finish()
                }, { throwable ->
                    if (throwable is ANError) {
                        AlertDialog.Builder(this).setMessage(throwable.errorBody).show()
                    }
                })
        }

    }

    fun setupWorkerSlotObserver() {
        viewModel.getWorkerSlot().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.slot = it.data
                    it.data?.let { it1 -> Log.d("mytag", it1.description) }
                    val belbinRoles: MutableList<String> = it.data?.roles?.let { it1 ->
                        getBelbinListByIndex(
                            it1
                        )
                    }!!

                    selectedRolesIds = it.data.roles
                    selectedSpecializationsIds = it.data.specializations

                    workerSlotEditRolesListView.adapter =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, belbinRoles)

                    val specializations: MutableList<String> =
                        it.data.specializations.let { it1 ->
                            getSpecializationsListByIndex(
                                it1
                            )
                        }
                    workerSlotEditSpecializationsListView.adapter = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        specializations
                    )

                    if (it.data.profile == null) {
                        workerSlotEditEmployeeTextView.visibility = View.GONE
                    }


                }
            }
        })
    }

    fun setupAppliesObserver() {
        viewModel.getApplies(token.toString()).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("mytag", it.data.toString())
                    it.data?.let { it1 -> renderList(it1) }
                }
                Status.ERROR -> {
                    Log.d("mytag", id.toString())
                }
            }
        })
    }

    fun setupViewModel() {
        viewModel = WorkerSlotViewModel(MainRepository(ApiHelper(ApiServiceImpl())), id)
    }

    @SuppressLint("CheckResult")
    fun applyUser(username: String, slotId: Int) {
        MainRepository(ApiHelper(ApiServiceImpl())).inviteProfile(
            username,
            slotId,
            token.toString()
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                Toast.makeText(
                    applicationContext,
                    "Пользователю отправлено приглашение на слот работника",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    @SuppressLint("CheckResult")
    fun declineUser(username: String, slotId: Int) {
        MainRepository(ApiHelper(ApiServiceImpl())).declineProfile(
            username,
            slotId,
            token.toString()
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                Toast.makeText(
                    applicationContext,
                    "Пользователь не принят на слот работника",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    fun showChangeBelbinDialog() {
        val tmpSelectedRoles = BooleanArray(allBelbinRoles.size)
        for (i in selectedRoles.indices) {
            tmpSelectedRoles[i] = selectedRoles[i]
        }

        var belbinArr = Array<String>(allBelbinRoles.size) { i -> allBelbinRoles[i].role }

        val builderMultiply = AlertDialog.Builder(this)
        builderMultiply.setTitle("Выберите требуемые роли")
        builderMultiply.setMultiChoiceItems(
            belbinArr,
            tmpSelectedRoles
        ) { dialog, which, isChecked ->
            selectedRoles[which] = isChecked
        }
        builderMultiply.setPositiveButton("Подтвердить") { dialog, id ->
            val newBelbin: MutableList<Int> = listOf<Int>().toMutableList()
            for (i in selectedRoles.indices) {
                if (selectedRoles[i]) {
                    newBelbin.add(allBelbinRoles[i].id)
                }
            }
            viewModel.getWorkerSlot().value?.data?.roles = newBelbin
            selectedRolesIds = newBelbin
            val roles = getBelbinListByIndex(newBelbin)
            workerSlotEditRolesListView.setAdapter(
                ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    roles
                )
            )
        }
        builderMultiply.show()
        Log.d("mytag", "show velbin")
    }

    fun showChangeSpecializationsDialog() {
        val tmpSelectedRoles = BooleanArray(allSpecializations.size)
        for (i in selectedSpecializations.indices) {
            tmpSelectedRoles[i] = selectedSpecializations[i]
        }
        val specializationArr =
            Array<String>(allSpecializations.size) { i -> allSpecializations[i].name }

        val builderMultiply = AlertDialog.Builder(this)
        builderMultiply.setTitle("Выберите требуемые специализации")
        builderMultiply.setMultiChoiceItems(
            specializationArr,
            tmpSelectedRoles
        ) { dialog, which, isChecked ->
            selectedSpecializations[which] = isChecked
        }
        builderMultiply.setPositiveButton("Подтвердить") { dialog, id ->
            val newSpecialization: MutableList<Int> = listOf<Int>().toMutableList()
            for (i in selectedSpecializations.indices) {
                if (selectedSpecializations[i]) {
                    newSpecialization.add(allSpecializations[i].id)
                }
            }
            viewModel.getWorkerSlot().value?.data?.specializations = newSpecialization
            selectedSpecializationsIds = newSpecialization
            val specializations = getSpecializationsListByIndex(newSpecialization)
            workerSlotEditSpecializationsListView.setAdapter(
                ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    specializations
                )
            )

        }
        builderMultiply.show()
    }

    @SuppressLint("CheckResult")
    fun getBelbinRoles() {
        MainRepository(ApiHelper(ApiServiceImpl())).getBelbilnRoles()
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                allBelbinRoles = response
                selectedRoles = BooleanArray(allBelbinRoles.size)
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
                selectedSpecializations = BooleanArray(allSpecializations.size)
                setupWorkerSlotObserver()
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

    private fun renderList(profiles: List<Profile>) {
        adapter.addData(profiles)
        adapter.notifyDataSetChanged()
    }

    private fun onListItemClick(position: Int) {
        val intent: Intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("username",
            viewModel.getApplies(token.toString()).value?.data?.get(position)?.user?.username
        )
        startActivity(intent)
    }

}