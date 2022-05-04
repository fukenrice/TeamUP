package com.example.kusashkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.ProjectModel
import kotlinx.android.synthetic.main.project_small_layout.view.*

class ProjectAdapter(private val projects: MutableList<ProjectModel>) :
    RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(project: ProjectModel) {
            itemView.projectSmallTitleTextView.text = "Проект " + project.title
            itemView.projectSmallDescriptionTextView.text = project.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.project_small_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ProjectAdapter.ViewHolder, position: Int) =
        holder.bind(projects[position])

    override fun getItemCount(): Int = projects.size

    fun addData(list: List<ProjectModel>) {
        projects.addAll(list)
    }

}