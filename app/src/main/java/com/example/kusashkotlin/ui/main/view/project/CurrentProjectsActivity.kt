package com.example.kusashkotlin.ui.main.view.project

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.adapter.ProjectAdapter
import com.example.kusashkotlin.ui.main.viewmodel.CurrentProjectsViewModel
import com.example.kusashkotlin.ui.main.viewmodel.ProjectListViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_current_projects.*

class CurrentProjectsActivity : AppCompatActivity() {

    lateinit var viewModel: CurrentProjectsViewModel
    lateinit var adapter: ProjectAdapter
    private lateinit var save: SharedPreferences
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        setContentView(R.layout.activity_current_projects)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        setupViewModel()
        setupObserver()
    }

    fun setupUI() {
        currentProjectsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProjectAdapter({position ->  onListItemClick(position)}, mutableListOf())
        currentProjectsRecyclerView.adapter = adapter
    }

    fun setupViewModel() {
        viewModel =
            CurrentProjectsViewModel(MainRepository(ApiHelper(ApiServiceImpl())), token.toString())
    }

    fun setupObserver() {
        viewModel.getProjects().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> renderList(it1) }
                }
            }
        })
    }

    private fun onListItemClick(position: Int) {
        val intent: Intent = Intent(this, ProjectActivity::class.java)
        intent.putExtra("title", viewModel.getProjects().value?.data?.get(position)?.title)
        startActivity(intent)
    }

    private fun renderList(projects: List<ProjectModel>) {
        adapter.clear()
        adapter.addData(projects)
        adapter.notifyDataSetChanged()
    }

}