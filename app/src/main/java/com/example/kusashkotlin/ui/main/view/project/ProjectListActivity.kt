package com.example.kusashkotlin.ui.main.view.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.SpecializationModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.adapter.ProjectAdapter
import com.example.kusashkotlin.ui.main.viewmodel.ProjectListViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_edit_worker_slot.*
import kotlinx.android.synthetic.main.activity_project_list.*

/// Не буду повторяться тут все тоже самое token, репоззиторий, константы, строки
class ProjectListActivity : AppCompatActivity() {

    private lateinit var viewModel: ProjectListViewModel
    private lateinit var adapter: ProjectAdapter
    private lateinit var projects: List<ProjectModel>
    private lateinit var allSpecializations: List<SpecializationModel>
    private lateinit var selectedSpecializations: BooleanArray
    private var selectedSpecializationsIds: List<Int> = mutableListOf()  // все данные должны быть во вьюмодели

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)
        projectListFilterButton.setOnClickListener {
            showFilterDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        projectListRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProjectAdapter({position ->  onListItemClick(position)}, mutableListOf())
        projectListRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.getProjects().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    projectsListProgressBar.visibility = View.GONE
                    projects = it.data!!
                    it.data.let { it1 -> renderList(it1) }
                }
                Status.LOADING -> {
                    projectsListProgressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    projectsListProgressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })

        viewModel.getSpecializations().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    projectListFilterButton.visibility = View.VISIBLE
                    allSpecializations = it.data!!
                    selectedSpecializations = BooleanArray(allSpecializations.size)
                }
            }
        })

    }

    private fun filterProjects(allProjects: List<ProjectModel>, requiresSpecializationsIds: List<Int>) : List<ProjectModel> {
        if (requiresSpecializationsIds.isEmpty()) {
            return allProjects
        }
        return allProjects.filter {
            it.requiredSpecialization.toSet().intersect(requiresSpecializationsIds).isNotEmpty()
        }
    }

    private fun showFilterDialog() {
        val tmpSelectedSpecializations = BooleanArray(allSpecializations.size)
        for (i in selectedSpecializations.indices) {
            tmpSelectedSpecializations[i] = selectedSpecializations[i]
        }
        val specializationArr =
            Array<String>(allSpecializations.size) { i -> allSpecializations[i].name }

        val builderMultiply = AlertDialog.Builder(this)
        builderMultiply.setTitle("Выберите специализации")
        builderMultiply.setMultiChoiceItems(
            specializationArr,
            tmpSelectedSpecializations
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
            selectedSpecializationsIds = newSpecialization // данные и вся логика во вьюмодели, тут только рендер
            renderList(filterProjects(projects, selectedSpecializationsIds))
        }
        builderMultiply.show()
    }

    private fun renderList(projects: List<ProjectModel>) {
        adapter.clear()
        adapter.addData(projects)
        adapter.notifyDataSetChanged()
    }


    private fun setupViewModel() {
        viewModel = ProjectListViewModel(MainRepository(ApiHelper(ApiServiceImpl())))
    }

    private fun onListItemClick(position: Int) {
        val intent: Intent = Intent(this, ProjectActivity::class.java)
        intent.putExtra("title", viewModel.getProjects().value?.data?.get(position)?.title)
        viewModel.getProjects().value?.data?.get(position)?.title?.let { Log.d("mytag", it) }
        startActivity(intent)
    }

}
