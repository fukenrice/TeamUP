package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.Profile
import kotlinx.android.synthetic.main.item_layout.view.*


class ProfileAdapter(private val profile: Profile) : RecyclerView.Adapter<ProfileAdapter.DataViewHolder>() {


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(profile: Profile) {
            itemView.nameTextView.text = profile.user.firstName
            itemView.surnameTextView.text = profile.user.lastName
            Glide.with(itemView.avatarImageView.context)
                .load(profile.photo)
                .into(itemView.avatarImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
//
//    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
//        holder.bind(users[position])
//
//    fun addData(list: List<User>) {
//        users.addAll(list)
//    }
}