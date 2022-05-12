package com.example.kusashkotlin.ui.main.view.project

import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.kusashkotlin.ui.main.view.profile.UserProfileActivity
import com.example.kusashkotlin.ui.main.viewmodel.ProjectListViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_project_list.*

class ProjectListActivity : AppCompatActivity() {

    private lateinit var viewModel: ProjectListViewModel
    private lateinit var adapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)
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