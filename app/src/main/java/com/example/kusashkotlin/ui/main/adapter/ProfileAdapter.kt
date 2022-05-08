package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.Profile
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.profile_small_layout.view.*

class ProfileAdapter(
    private val onApplyClick: (username: String, slotId: Int) -> Unit,
    private val onDenyClick: (username: String, slotId: Int) -> Unit,
    private val onItemClicked: (position: Int) -> Unit,
    private val profiles: MutableList<Profile>,
    private val slotId: Int
) :
    RecyclerView.Adapter<ProfileAdapter.DataViewHolder>() {


    inner class DataViewHolder(private val onItemClicked: (position: Int) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(profile: Profile) {
            itemView.profileSmallNameTextView.text =
                profile.user.firstName + " " + profile.user.lastName
            itemView.profileSmallDescriptionTextView.text = profile.desctiption
            itemView.profileSmallApplyButton.setOnClickListener { onApplyClick(profiles[adapterPosition].user.username, slotId) }
            itemView.profileSmallDenyButton.setOnClickListener { onDenyClick(profiles[adapterPosition].user.username, slotId) }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(onItemClicked,
            LayoutInflater.from(parent.context).inflate(
                R.layout.profile_small_layout, parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(profiles[position])
    }

    override fun getItemCount(): Int = profiles.size

    fun addData(list: List<Profile>) {
        profiles.addAll(list)
    }
}
