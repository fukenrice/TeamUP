package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

sealed class ProjectEditAction {
    object ChangeSpecialization : ProjectEditAction()
    object ChangeRoles : ProjectEditAction()
    object AddSlot : ProjectEditAction()
    data class ViewSlot(val id: Int) : ProjectEditAction()
    object ConfirmChanges : ProjectEditAction()
    object DeleteProject : ProjectEditAction()
}
