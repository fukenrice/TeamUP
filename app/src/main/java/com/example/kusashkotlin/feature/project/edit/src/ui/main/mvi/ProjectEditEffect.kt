package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel

sealed class ProjectEditEffect {
    data class BootstrapData(val project: ProjectModel?) : ProjectEditEffect()
    data class ChangeSpecializations(val selectedIds : List<Int>) : ProjectEditEffect()
    data class ChangeRoles(val selectedIds : List<Int>) : ProjectEditEffect()
    data class DisplaySpec(val specializations: List<Int>) : ProjectEditEffect()
    data class DisplayRoles(val roles: List<Int>) : ProjectEditEffect()
    object AddSlot : ProjectEditEffect()
    data class ViewSlot(val id: Int) : ProjectEditEffect()
    object DeleteProject : ProjectEditEffect()
    data class ConfirmChanges(val project: ProjectModel?) : ProjectEditEffect()
}