package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.WorkerSlot
import kotlinx.android.synthetic.main.applied_worker_slot_layout.view.*
import kotlinx.android.synthetic.main.applied_worker_slot_small_layout.view.*

/// Аналогичные коментарии что и для других адаптеров
class RequestedWorkerSlotAdapter(
    private val onDenyClick: (slotId: Int, position: Int) -> Unit,
    private val onItemClicked: (position: Int) -> Unit,
    private val slots: MutableList<WorkerSlot>,
) : RecyclerView.Adapter<RequestedWorkerSlotAdapter.DataViewHolder>() {

    inner class DataViewHolder(private val onItemClicked: (position: Int) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(slot: WorkerSlot) {
            itemView.appliedWorkerSlotSmallEmployeeName.text =
                slot.profile
            itemView.appliedWorkerSlotSmallDescriptionTextView.text = slot.description

            itemView.appliedWorkerSlotSmallRetractButton.setOnClickListener { slot.id?.let { it1 ->
                onDenyClick(
                    it1, adapterPosition)
            } }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(onItemClicked,
            LayoutInflater.from(parent.context).inflate(
                R.layout.applied_worker_slot_small_layout, parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) = holder.bind(slots[position])

    fun addData(list: List<WorkerSlot>) {
        slots.addAll(list)
    }

    fun removeItem(position: Int) {
        slots.removeAt(position)
    }

    fun clear() {
        slots.clear()
    }

    override fun getItemCount(): Int = slots.size

}
