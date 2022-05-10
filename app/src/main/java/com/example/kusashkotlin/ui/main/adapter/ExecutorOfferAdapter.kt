package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.ExecutorOffer
import kotlinx.android.synthetic.main.executor_offer_small_layout.view.*

class ExecutorOfferAdapter(
    private val onItemClicked: (position: Int) -> Unit,
    private val offers: MutableList<ExecutorOffer>
) :
    RecyclerView.Adapter<ExecutorOfferAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(offer: ExecutorOffer) {
            itemView.executorOfferSmallNameTextView.text = offer.username
            itemView.executorOfferSmallDescriptionTextView.text = offer.description
            itemView.executorOfferSmallSalaryTextView.text =
                "Пердполагаемыя зарплата: ${offer.salary.toString()}"
            itemView.executorOfferSmallHoursTextView.text =
                "Хочет работать: ${offer.workHours.toString()}"
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExecutorOfferAdapter.ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.executor_offer_small_layout, parent, false), onItemClicked
    )

    override fun onBindViewHolder(holder: ExecutorOfferAdapter.ViewHolder, position: Int) =
        holder.bind(offers[position])

    override fun getItemCount(): Int = offers.size

    fun addData(list: List<ExecutorOffer>) {
        offers.addAll(list)
    }

    fun clear() {
        offers.clear()
    }
}