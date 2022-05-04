package com.example.kusashkotlin.ui.main.view.project

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.adapter.ProjectAdapter
import com.example.kusashkotlin.ui.main.viewmodel.ProjectListViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_project_list.*

class ProjectListActivity : AppCompatActivity() {

    lateinit var viewModel: ProjectListViewModel
    lateinit var adapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)
        setupUI()
        setupViewModel()
        setupObserver()
        // TODO: Обрабатывать нажатие на элемент и запускать активити просмотра
    }


    private fun setupUI() {
        projectListRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProjectAdapter(mutableListOf())
        projectListRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.getProjects().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    projectsListProgressBar.visibility = View.GONE
                    it.data?.let { it1 -> renderList(it1) }
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
    }

    private fun renderList(projects: List<ProjectModel>) {
        adapter.addData(projects)
        adapter.notifyDataSetChanged()
    }


    private fun setupViewModel() {
        viewModel = ProjectListViewModel(MainRepository(ApiHelper(ApiServiceImpl())))
    }


}