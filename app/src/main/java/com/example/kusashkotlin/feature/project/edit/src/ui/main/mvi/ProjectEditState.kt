package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel

data class ProjectEditState(
    val project: ProjectModel,
) {
    companion object {
        val initial = ProjectEditState(ProjectModel())
    }
}
