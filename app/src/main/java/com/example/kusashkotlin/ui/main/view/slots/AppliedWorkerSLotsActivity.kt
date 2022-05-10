package com.example.kusashkotlin.ui.main.view.slots;

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kusashkotlin.R;
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.WorkerSlot
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.adapter.AppliedWorkerSlotAdapter
import com.example.kusashkotlin.ui.main.viewmodel.AppliedSlotsViewModel
import com.example.kusashkotlin.ui.main.viewmodel.ProjectViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_applied_worker_slots.*

class AppliedWorkerSLotsActivity : AppCompatActivity() {

    lateinit var viewModel: AppliedSlotsViewModel

    private lateinit var save: SharedPreferences

    private var token: String? = null

    private lateinit var adapter: AppliedWorkerSlotAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        save = getSharedPreferences("APP", MODE_PRIVATE)
        token = save.getString("token", "")
        setContentView(R.layout.activity_applied_worker_slots);

    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupViewModel()
        setupObserver()
    }


    private fun setupObserver() {
        viewModel.getSlots().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> renderList(it1) }
                    Log.d("mytag", it.data.toString())
                }
                Status.ERROR -> {
                    Log.d("mytag", it.message.toString())
                }
            }
        })
    }

    private fun setupUI() {
        appliedWorkerSlotsRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AppliedWorkerSlotAdapter(
            { slotId, position ->
                viewModel.acceptInvite(slotId)
                removeItem(position)
                },
            { slotId, position ->
                viewModel.declineInvite(slotId)
                removeItem(position)
            },
            { position -> onItemClicked(position) },
            mutableListOf()
        )

        appliedWorkerSlotsRecyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel = AppliedSlotsViewModel(
            MainRepository(ApiHelper(ApiServiceImpl())),
            token.toString(),
            this
        )
    }

    private fun onItemClicked(position: Int) {
        val intent: Intent = Intent(this, WorkerSlotActivity::class.java)
        intent.putExtra("id", viewModel.getSlots().value?.data?.get(position)?.id)
        startActivity(intent)
    }

    private fun removeItem(position: Int) {
        adapter.removeItem(position)
        adapter.notifyDataSetChanged()
    }

    private fun renderList(slots: List<WorkerSlot>) {
        adapter.clear()
        adapter.addData(slots)
        adapter.notifyDataSetChanged()
    }
}
