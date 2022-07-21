package com.example.kusashkotlin.ui.main.view.profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProfileUpdate
import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel
import com.example.kusashkotlin.data.model.User
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.databinding.ActivityEditBinding
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import com.example.kusashkotlin.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit_project.*


/// Не буду повторяться тут все тоже самое token, репоззиторий, константы, строки
class EditActivity : AppCompatActivity() {

    lateinit var allSpecializations: List<SpecializationModel>

    lateinit var selectedSpecializations: BooleanArray

    private var selectedSpecializationsIds: List<Int> = listOf()

    private lateinit var binding: ActivityEditBinding

    lateinit var viewModel: ProfileViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        getSpecializations()
        // TODO: Сделать изменение пароля
    }


    private fun setupProfileObserver() {
        viewModel.getProfile().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    binding.profile = it.data
                    if (it.data?.specialzation != null) {
                        specializationEditListView.adapter = ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1,
                            it.data.specialzation!!
                        )
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }



            }
        })
    }

    @SuppressLint("CheckResult")
    private fun setContent() {
        setupViewModel()
        setupProfileObserver()
        specializationEditButton.setOnClickListener {
            showChangeSpecializationsDialog()
        }
        buttonEditConfirm.setOnClickListener {
            viewModel.getProfile().value?.data?.user?.let { it1 ->
            }

            var remote: Int? = null

            if (remoteRadioGroup.indexOfChild(remoteRadioGroup.findViewById(remoteRadioGroup.checkedRadioButtonId)) != -1) {
                remote =
                    remoteRadioGroup.indexOfChild(remoteRadioGroup.findViewById(remoteRadioGroup.checkedRadioButtonId)) + 1
            }

            var isMale: Boolean? = null

            if (sexRadioGroup.indexOfChild(sexRadioGroup.findViewById(sexRadioGroup.checkedRadioButtonId)) != -1) {
                isMale =
                    sexRadioGroup.indexOfChild(sexRadioGroup.findViewById(sexRadioGroup.checkedRadioButtonId)) == 0
            }

            var specializations: List<Int>? = null
            if (!selectedSpecializationsIds.isEmpty()) {
                specializations = selectedSpecializationsIds
            }
            val update = ProfileUpdate(
                remote = remote,
                isMale = isMale,
                patronymic = patronymicEditTextEdit.text.toString(),
                user = User(
                    firstName = editNameEditText.text.toString(),
                    lastName = lastnameEditEditText.text.toString(),
                    email = emailEditTextEdit.text.toString()
                ),
                city = cityEditTextEdit.text.toString(),
                age = ageEditTextEdit.text.toString().toIntOrNull(),
                description = descriptionEditTextEdit.text.toString(),
                specialization = specializations
            )


            MainRepository(ApiHelper(ApiServiceImpl())).editProfile(update, token.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG)
                        .show()
                    finish()
                }, { throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                            .show() // Обработать все json теги
                    }
                })
        }

        radioEmpty.setOnClickListener(onRadioButtonClicked())
        radioFemale.setOnClickListener(onRadioButtonClicked())
        radioMale.setOnClickListener(onRadioButtonClicked())
        radioNo.setOnClickListener(onRadioButtonClicked())
        radioYes.setOnClickListener(onRadioButtonClicked())
    }

    private fun onRadioButtonClicked(): View.OnClickListener? {
        return View.OnClickListener { }
    }

    private fun setupViewModel() {
        viewModel = ProfileViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            save.getString("username", "").toString()
        )
    }

    @SuppressLint("CheckResult")
    fun getSpecializations() {
        MainRepository(ApiHelper(ApiServiceImpl())).getSpecializations()
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                allSpecializations = response
                selectedSpecializations = BooleanArray(allSpecializations.size)
                setContent()
            }, { throwable ->
                if (throwable is ANError) {
                    Toast.makeText(applicationContext, throwable.errorBody, Toast.LENGTH_LONG)
                        .show() // TODO: Обработать все json теги
                }
            })
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
            selectedSpecializationsIds = newSpecialization
            val specializations = getSpecializationsListByIndex(newSpecialization)
            specializationEditListView.setAdapter(
                ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    specializations
                )
            )
        }
        builderMultiply.show()
    }


}
