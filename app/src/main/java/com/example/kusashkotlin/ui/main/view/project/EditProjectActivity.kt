package com.example.kusashkotlin.ui.main.view.project

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityEditProjectBinding
import com.example.kusashkotlin.ui.main.viewmodel.ProjectViewModel
import com.example.kusashkotlin.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_project.*
import kotlinx.coroutines.runBlocking

class EditProjectActivity : AppCompatActivity() {

    lateinit var allBelbinRoles: List<RoleModel>

    private lateinit var selectedRoles: BooleanArray

    private lateinit var selectedRolesIds: List<Int>

    lateinit var allSpecializations: List<SpecializationModel>

    lateinit var selectedSpecializations: BooleanArray

    lateinit var selectedSpecializationsIds: List<Int>

    lateinit var viewModel: ProjectViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    private lateinit var binding: ActivityEditProjectBinding

    private var projectTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_project)
        getBelbinRoles()
        getSpecializations()
        setContent()
    }

    @SuppressLint("CheckResult")
    private fun fetchProjectTitle() {
        MainRepository(ApiHelper(ApiServiceImpl())).getProfile(
            save.getString("username", "").toString()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ profile ->
                if (profile.project != null) {
                    projectTitle = profile.project
                }
                setupViewModel()
                setupObserver()
            }, { throwable ->
                Toast.makeText(applicationContext, "Ошибка получения проекта", Toast.LENGTH_LONG)
                    .show()
            })
    }


    @SuppressLint("CheckResult")
    fun setContent() {
        fetchProjectTitle()
        projectEditChangeBelbinButton.setOnClickListener {
            showChangeBelbinDialog()
        }
        projectEditSpecializationChangeButton.setOnClickListener {
            showChangeSpecializationsDialog()
        }

        projectEditDeleteButton.setOnClickListener {
            MainRepository(ApiHelper(ApiServiceImpl())).deleteProject(token.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(applicationContext, "Проект удален", Toast.LENGTH_LONG).show()
                    finish()
                }, { throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                            .show() // TODO: Обработать все json теги
                    }
                })
        }

        projectEditConfirmButton.setOnClickListener {

            var message: String = ""
            if (projectEditProjectTitleTextView.text.isEmpty()) {
                message += "Пожалуйста, введите название пероекта\n"
            }
            if (projectEditDescriptionTextEdit.text.isEmpty()) {
                message += "Пожалуйста, введите описание проекта\n"
            }
            if (projectEditVacantTextEdit.text.isEmpty()) {
                message += "Пожалуйста, введите количество свободных мест\n"
            }
            if (projectEditBelbinListView.isEmpty()) {
                message += "Пожалуйста, заполните роли по Белбину\n"
            }
            if (projectEditSpecializationsListView.isEmpty()) {
                message += "Пожалуйста, заполните теребуемые специализации\n"
            }

            if (message != "") {
                AlertDialog.Builder(this).setMessage(message).create().show()
                return@setOnClickListener
            }

            val project: ProjectModel

            val remote: Boolean?
            if (projectEditRemoteRadioGroup.indexOfChild(
                    projectEditRemoteRadioGroup.findViewById(
                        projectEditRemoteRadioGroup.checkedRadioButtonId
                    )
                ) == -1 || projectEditRemoteRadioGroup.indexOfChild(
                    projectEditRemoteRadioGroup.findViewById(
                        projectEditRemoteRadioGroup.checkedRadioButtonId
                    )
                ) == 0
            ) {
                remote = null
            } else {
                remote = projectEditRemoteRadioGroup.indexOfChild(
                    projectEditRemoteRadioGroup.findViewById(
                        projectEditRemoteRadioGroup.checkedRadioButtonId
                    )
                ) == 1
            }
            project = ProjectModel(
                title = projectEditProjectTitleTextEdit.text.toString(),
                description = projectEditDescriptionTextEdit.text.toString(),
                vacant = projectEditVacantTextEdit.text.toString().toIntOrNull(),
                city = projectEditCityTextEdit.text.toString(),
                online = remote,
                requiredSpecialization = selectedSpecializationsIds,
                requiredBelbin = selectedRolesIds
            )


            MainRepository(ApiHelper(ApiServiceImpl())).updateProject(project, token.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(
                        applicationContext,
                        "Проект успешно отредактирован",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }, { throwable ->
                    if (throwable is ANError) {
                        Log.d("error", projectTitle)
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                            .show() // TODO: Обработать все json теги
                    }
                })
        }


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
            Log.d("belbin", newBelbin[0].toString())
            viewModel.getProject().value?.data?.requiredBelbin = newBelbin
            selectedRolesIds = newBelbin
            val roles = getBelbinListByIndex(newBelbin)
            projectEditBelbinListView.setAdapter(
                ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    roles
                )
            )
        }
        builderMultiply.show()
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
            viewModel.getProject().value?.data?.requiredSpecialization = newSpecialization
            selectedSpecializationsIds = newSpecialization
            val specializations = getSpecializationsListByIndex(newSpecialization)
            projectEditSpecializationsListView.setAdapter(
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
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show() // TODO: Обработать все json теги
                }
            })
    }

    fun setupViewModel() {
        viewModel = ProjectViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            projectTitle
        )
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

    fun setupObserver() {
        viewModel.getProject().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.project = it.data

                    // создаем массивы стрингов для отображения в лист вью
                    val belbinRoles: MutableList<String> = it.data?.requiredBelbin?.let { it1 ->
                        getBelbinListByIndex(
                            it1
                        )
                    }!!

                    projectEditBelbinListView.adapter =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, belbinRoles)

                    selectedSpecializationsIds = it.data.requiredSpecialization
                    selectedRolesIds = it.data.requiredBelbin

                    val specializations: MutableList<String> =
                        it.data.requiredSpecialization.let { it1 ->
                            getSpecializationsListByIndex(
                                it1
                            )
                        }
                    projectEditSpecializationsListView.adapter = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        specializations
                    )
                }
            }
        })
    }


}