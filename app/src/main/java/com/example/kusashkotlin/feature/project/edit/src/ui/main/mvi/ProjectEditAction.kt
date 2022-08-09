package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.example.kusashkotlin.data.model.ProjectModel

sealed class ProjectEditAction {
    object ChangeSpecialization : ProjectEditAction()
    object ChangeRoles : ProjectEditAction()
    data class ConfirmSpecializations(val newSpecializations: List<Int>) : ProjectEditAction()
    data class ConfirmRoles(val newRoles: List<Int>) : ProjectEditAction()
    object AddSlot : ProjectEditAction()
    data class ViewSlot(val id: Int) : ProjectEditAction()
    data class ConfirmChanges(val project: ProjectModel) : ProjectEditAction()
    object DeleteProject : ProjectEditAction()
}
