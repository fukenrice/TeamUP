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
import com.example.kusashkotlin.data.model.WorkerSlot
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.adapter.RequestedWorkerSlotAdapter
import com.example.kusashkotlin.ui.main.adapter.WorkerSlotAdapter
import com.example.kusashkotlin.ui.main.viewmodel.ProjectViewModel
import com.example.kusashkotlin.ui.main.viewmodel.RequestedWorkerSlotsViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_sent_requests_workers_slots.*

class SentRequestsWorkersSlotsActivity : AppCompatActivity() {

    lateinit var adapter: RequestedWorkerSlotAdapter

    lateinit var viewModel: RequestedWorkerSlotsViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        setContentView(R.layout.activity_sent_requests_workers_slots)
    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        viewModel = RequestedWorkerSlotsViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            token.toString(),
            this
        )
    }

    private fun setupUI() {
        requestedSlotsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RequestedWorkerSlotAdapter({ slotId, position ->
            viewModel.retractApply(slotId)
            removeItem(position)
        }, { position -> onListItemClick(position) }, mutableListOf())
        requestedSlotsRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.getSlots().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> renderList(it1) }
                }
            }
        })
    }

    private fun removeItem(position: Int) {
        adapter.removeItem(position)
        adapter.notifyDataSetChanged()
    }

    private fun onListItemClick(position: Int) {
        val intent: Intent = Intent(this, WorkerSlotActivity::class.java)
        intent.putExtra("id", viewModel.getSlots().value?.data?.get(position)?.id)
        startActivity(intent)
    }

    private fun renderList(slots: List<WorkerSlot>) {
        adapter.clear()
        adapter.addData(slots)
        adapter.notifyDataSetChanged()
    }

}