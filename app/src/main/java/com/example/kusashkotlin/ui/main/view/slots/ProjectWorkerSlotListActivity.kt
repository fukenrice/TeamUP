package com.example.kusashkotlin.ui.main.view.slots

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
import com.example.kusashkotlin.data.model.WorkerSlot
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.adapter.ProjectAdapter
import com.example.kusashkotlin.ui.main.adapter.ProjectWorkerSlotAdapter
import com.example.kusashkotlin.ui.main.adapter.WorkerSlotAdapter
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel
import com.example.kusashkotlin.ui.main.viewmodel.ProjectViewModel
import com.example.kusashkotlin.ui.main.viewmodel.WorkerSlotViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_project_worker_slot_list.*

class ProjectWorkerSlotListActivity : AppCompatActivity() {
    lateinit var viewModel: ProjectViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    private lateinit var adapter: ProjectWorkerSlotAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_worker_slot_list)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupViewModel()
        setupObserver()
    }

    fun setupUI() {
        projectWorkerSlotsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProjectWorkerSlotAdapter({ position -> onListItemClick(position) },
            { id ->
                viewModel.inviteUser(
                    intent.getStringExtra("username").toString(),
                    id,
                    token.toString(),
                    this
                )
            }, mutableListOf())
        projectWorkerSlotsRecyclerView.adapter = adapter
    }

    fun setupObserver() {
        viewModel.getProject().observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> renderList(it1.team) }
                }
            }
        })
    }

    fun setupViewModel() {
        viewModel = ProjectViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            intent.getStringExtra("project_title").toString()
        )
    }

    private fun onListItemClick(position: Int) {
        val intent: Intent = Intent(this, WorkerSlotActivity::class.java)
        intent.putExtra("id", viewModel.getProject().value?.data?.team?.get(position)?.id)
        startActivity(intent)
    }

    private fun renderList(slots: List<WorkerSlot>) {
        adapter.clear()
        adapter.addData(slots)
        adapter.notifyDataSetChanged()
    }

}