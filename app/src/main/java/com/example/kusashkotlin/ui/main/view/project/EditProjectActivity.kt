package com.example.kusashkotlin.ui.main.view.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.WorkerSlot
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.data.repo.PreferencesRepository
import com.example.kusashkotlin.databinding.ActivityEditProjectBinding
import com.example.kusashkotlin.feature.project.edit.src.mvi.Event
import com.example.kusashkotlin.feature.project.edit.src.ui.main.ProjectEditViewModel
import com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi.*
import com.example.kusashkotlin.ui.main.adapter.WorkerSlotAdapter
import com.example.kusashkotlin.ui.main.view.slots.EditWorkerSlotActivity

/// Не буду повторяться тут все тоже самое token, репоззиторий, константы, строки
class EditProjectActivity : AppCompatActivity() {
    // TODO получать из интента имя проекта, перед запуском активити цеплять профиль
    private lateinit var binding: ActivityEditProjectBinding

    private val viewModel = ProjectEditViewModel(
        ProjectEditFeatureFactory(
            ProjectEditActor(
                MainRepository(ApiHelper(ApiServiceImpl())), PreferencesRepository()
            )
        )
    )

    private val adapter = WorkerSlotAdapter(
        { id -> viewModel.acceptAction(ProjectEditAction.ViewSlot(id)) },
        mutableListOf()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProjectBinding.inflate(layoutInflater).apply {
            projectEditWorkerSlotsRecyclerView.apply {
                adapter = adapter
                layoutManager = LinearLayoutManager(context)
            }
            projectEditSpecializationChangeButton.setOnClickListener {
                viewModel.acceptAction(
                    ProjectEditAction.ChangeSpecialization
                )
            }
            projectEditChangeBelbinButton.setOnClickListener {
                viewModel.acceptAction(
                    ProjectEditAction.ChangeRoles
                )
            }
            projectEditAddWorkerSlotButton.setOnClickListener {
                viewModel.acceptAction(
                    ProjectEditAction.AddSlot
                )
            }
            projectEditConfirmButton.setOnClickListener {

                val remote: Boolean?
                val remoteRadio = projectEditRemoteRadioGroup.indexOfChild(
                    projectEditRemoteRadioGroup.findViewById(
                        projectEditRemoteRadioGroup.checkedRadioButtonId
                    )
                )
                if (remoteRadio == -1 || remoteRadio == 0) {
                    remote = null
                } else {
                    remote = remoteRadio == 1
                }

                viewModel.acceptAction(
                    ProjectEditAction.ConfirmChanges(
                        ProjectModel(
                            title = binding.projectEditProjectTitleTextEdit.text.toString(),
                            description = projectEditDescriptionTextEdit.text.toString(),
                            vacant = projectEditVacantTextEdit.text.toString().toIntOrNull(),
                            city = projectEditCityTextEdit.text.toString(),
                            online = remote
                        )
                    )
                )
            }
            projectEditDeleteButton.setOnClickListener { viewModel.acceptAction(ProjectEditAction.DeleteProject) }
        }
        setContentView(binding.root)
        viewModel.observe(
            lifecycleScope,
            ::onEvent,
            ::onState
        )
    }

    private fun onEvent(event: Event) {
        when (event) {
            is AddSlotEvent -> {
                val intent = Intent(this, EditWorkerSlotActivity::class.java)
                intent.putExtra("mode", "create")
                startActivity(intent)
            }
            is ViewSlotEvent -> {
                val intent: Intent = Intent(this, EditWorkerSlotActivity::class.java)
                intent.putExtra("mode", "edit")
                intent.putExtra("id", event.id)
                startActivity(intent)
            }
            is DeleteProjectEvent -> {
                Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                if (event.success) {
                    finish()
                }
            }
            is ConfirmChangesEvent -> {
                Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                if (event.success) {
                    finish()
                }
            }
            is DisplayChangeRolesDialog -> {
                displayRolesDialog(event.selectedIds)
            }
            is DisplayChangeSpecializationsDialog -> {
                displaySpecializationsDialog(event.selectedIds)
            }
        }
    }

    private fun onState(state: ProjectEditState) {
        binding.apply {
            project = state.project
            renderList(state.project.team)
            projectEditSpecializationsListView.adapter = ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_list_item_1,
                viewModel.getSpecializationsListByIndex(state.project.requiredSpecialization)
            )
            projectEditBelbinListView.adapter = ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_list_item_1,
                viewModel.getBelbinListByIndex(state.project.requiredBelbin)
            )
        }
    }

    private fun renderList(slots: List<WorkerSlot>) {
        adapter.clear()
        adapter.addData(slots)
        adapter.notifyDataSetChanged()
    }

    private fun displaySpecializationsDialog(ids: List<Int>) {
        val allSpecializations = PreferencesRepository().getAllSpecializations()!!
        val selectedSpecializations = BooleanArray(allSpecializations.size)
        val specializationArr =
            Array<String>(allSpecializations.size) { i -> allSpecializations[i].name }
        for (i in allSpecializations.indices) {
            if (allSpecializations[i].id in ids) {
                selectedSpecializations[i] = true
            }
        }

        val builderMultiply = AlertDialog.Builder(this)
        builderMultiply.setTitle("Выберите требуемые специализации")

        builderMultiply.setMultiChoiceItems(
            specializationArr,
            selectedSpecializations
        ) { dialog, which, isChecked ->
            selectedSpecializations[which] = isChecked
        }

        builderMultiply.setPositiveButton("Подтвердить") { dialog, id ->
            val newSpecialization: MutableList<Int> = mutableListOf()
            for (i in selectedSpecializations.indices) {
                if (selectedSpecializations[i]) {
                    newSpecialization.add(allSpecializations[i].id)
                }
            }
            viewModel.acceptAction(ProjectEditAction.ConfirmSpecializations(newSpecialization))
        }
        builderMultiply.show()
    }

    private fun displayRolesDialog(ids: List<Int>) {
        val allRoles = PreferencesRepository().getAllRoles()!!
        val selectedRoles = BooleanArray(allRoles.size)
        val rolesArr =
            Array<String>(allRoles.size) { i -> allRoles[i].role }
        for (i in allRoles.indices) {
            if (allRoles[i].id in ids) {
                selectedRoles[i] = true
            }
        }

        val builderMultiply = AlertDialog.Builder(this)
        builderMultiply.setTitle("Выберите требуемые роли")

        builderMultiply.setMultiChoiceItems(
            rolesArr,
            selectedRoles
        ) { dialog, which, isChecked ->
            selectedRoles[which] = isChecked
        }

        builderMultiply.setPositiveButton("Подтвердить") { dialog, id ->
            val newRoles: MutableList<Int> = mutableListOf()
            for (i in selectedRoles.indices) {
                if (selectedRoles[i]) {
                    newRoles.add(allRoles[i].id)
                }
            }
            viewModel.acceptAction(ProjectEditAction.ConfirmRoles(newRoles))
        }
        builderMultiply.show()
    }
}
