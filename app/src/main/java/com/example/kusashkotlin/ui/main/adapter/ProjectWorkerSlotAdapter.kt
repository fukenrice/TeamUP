package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.WorkerSlot
import kotlinx.android.synthetic.main.project_worker_slot_small_layout.view.*
import kotlinx.android.synthetic.main.worker_slot_small_layout.view.*

class ProjectWorkerSlotAdapter(private val onItemClicked: (position: Int) -> Unit,
                               private val onInviteClicked: (id: Int) -> Unit,
                               private val slots: MutableList<WorkerSlot>,
) : RecyclerView.Adapter<ProjectWorkerSlotAdapter.DataViewHolder>() {

    inner class DataViewHolder(private val onItemClicked: (position: Int) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(slot: WorkerSlot) {
            if (slot.profile == null) {
                itemView.projectWorkerSlotSmallEmployeeName.text = "Место свободно"
            } else {
                itemView.projectWorkerSlotSmallEmployeeName.text = slot.profile.toString()
            }
            itemView.projectWorkerSlotSmallDescriptionTextView.text = slot.description
            itemView.projectWorkerSlotInviteButton.setOnClickListener { slot.id?.let { it1 ->
                onInviteClicked(
                    it1
                )
            } }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataViewHolder = DataViewHolder(
        onItemClicked,
        LayoutInflater.from(parent.context)
            .inflate(R.layout.project_worker_slot_small_layout, parent, false)
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
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