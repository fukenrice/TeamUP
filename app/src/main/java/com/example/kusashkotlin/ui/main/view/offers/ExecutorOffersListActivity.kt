package com.example.kusashkotlin.ui.main.view.offers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ExecutorOffer
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.adapter.ExecutorOfferAdapter
import com.example.kusashkotlin.ui.main.viewmodel.ExecutorOfferListViewModel
import com.example.kusashkotlin.utils.Status
import kotlinx.android.synthetic.main.activity_executor_offers_list.*
import kotlinx.android.synthetic.main.activity_project_list.*

class ExecutorOffersListActivity : AppCompatActivity() {

    lateinit var viewModel: ExecutorOfferListViewModel
    lateinit var adapter: ExecutorOfferAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_executor_offers_list)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        executorOffersListRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExecutorOfferAdapter(mutableListOf())
        executorOffersListRecyclerView.adapter = adapter
    }



    fun setupObserver() {
        viewModel.getOffers().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    executorOfferListProgressBar.visibility = View.GONE
                    it.data?.let { it1 -> renderList(it1) }
                }
                Status.LOADING -> {
                    executorOfferListProgressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    executorOfferListProgressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })
    }

    fun setupViewModel() {
        viewModel = ExecutorOfferListViewModel(MainRepository(ApiHelper(ApiServiceImpl())))
    }

    private fun renderList(offers: List<ExecutorOffer>) {
        adapter.addData(offers)
        adapter.notifyDataSetChanged()
    }



}