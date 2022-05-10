package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.WorkerSlot
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.worker_slot_small_layout.view.*

class WorkerSlotAdapter(private val onItemClicked: (position: Int) -> Unit, private val slots: MutableList<WorkerSlot>) : RecyclerView.Adapter<WorkerSlotAdapter.DataViewHolder>() {

    class DataViewHolder(private val onItemClicked: (position: Int) -> Unit, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(slot: WorkerSlot) {
            if (slot.profile == null) {
                itemView.workerSlotSmallEmployeeName.text = "Место свободно"
            } else {
                itemView.workerSlotSmallEmployeeName.text = slot.profile.toString()
            }
            itemView.workerSlotSmallDescriptionTextView.text = slot.description
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkerSlotAdapter.DataViewHolder = DataViewHolder(onItemClicked,
        LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_slot_small_layout, parent, false)
    )

    override fun onBindViewHolder(holder: WorkerSlotAdapter.DataViewHolder, position: Int) {
        holder.bind(slots[position])
    }

    override fun getItemCount(): Int = slots.size

    fun addData(list: List<WorkerSlot>) {
        slots.addAll(list)
    }

    fun clear() {
        slots.clear()
    }
}