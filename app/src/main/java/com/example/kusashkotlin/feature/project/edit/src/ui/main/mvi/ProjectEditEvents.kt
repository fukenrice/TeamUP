package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.feature.project.edit.src.mvi.Event

object AddSlotEvent : Event

data class ViewSlotEvent(val id : Int) : Event

data class DeleteProjectEvent(val message : String?, val success : Boolean) : Event

data class ConfirmChangesEvent(val message : String?, val success : Boolean) : Event

data class DisplayChangeRolesDialog(val selectedIds : List<Int>) : Event

data class DisplayChangeSpecializationsDialog(val selectedIds: List<Int>) : Event