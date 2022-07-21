package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.model.WorkerSlot
import kotlinx.android.synthetic.main.applied_worker_slot_layout.view.*

class AppliedWorkerSlotAdapter(
    private val onApplyClick: (slotId: Int, position: Int) -> Unit,
    private val onDenyClick: (slotId: Int, position: Int) -> Unit,
    private val onItemClicked: (position: Int) -> Unit,
    private val slots: MutableList<WorkerSlot>,
) : RecyclerView.Adapter<AppliedWorkerSlotAdapter.DataViewHolder>() {


    // onItemClicked - не нужен в конструкторе, можно так же как и с остальными методами напрямую вызвать
    inner class DataViewHolder(private val onItemClicked: (position: Int) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        // чтобы itemView каждый раз не пистаь, можно через with сделать
        fun bind(slot: WorkerSlot) = with(itemView) {
            appliedWorkerSlotProjectNameTextView.text = slot.profile
            appliedWorkerSlotDescriptionTextView.text = slot.description
            appliedWorkerSlotApplyButton.setOnClickListener { slot.id?.let { it1 ->
                onApplyClick(it1, adapterPosition)
            } }
            appliedWorkerSlotDenyButton.setOnClickListener { slot.id?.let { it1 ->
                onDenyClick(it1, adapterPosition)
            } }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(onItemClicked,
            LayoutInflater.from(parent.context).inflate(
                R.layout.applied_worker_slot_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = slots.size

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

}
