package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.ExecutorOffer
import kotlinx.android.synthetic.main.executor_offer_small_layout.view.*

class ExecutorOfferAdapter(private val offers: MutableList<ExecutorOffer>) :
    RecyclerView.Adapter<ExecutorOfferAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(offer: ExecutorOffer) {
            itemView.executorOfferSmallNameTextView.text = offer.username
            itemView.executorOfferSmallDescriptionTextView.text = offer.description
            itemView.executorOfferSmallSalaryTextView.text =
                "Пердполагаемыя зарплата: ${offer.salary.toString()}"
            itemView.executorOfferSmallHoursTextView.text =
                "Хочет работать: ${offer.workHours.toString()}"
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExecutorOfferAdapter.ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.executor_offer_small_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ExecutorOfferAdapter.ViewHolder, position: Int) = holder.bind(offers[position])

    override fun getItemCount(): Int = offers.size

    fun addData(list: List<ExecutorOffer>) {
        offers.addAll(list)
    }

}