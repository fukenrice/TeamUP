package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.feature.project.edit.src.mvi.Event

object AddSlotEvent : Event

data class ViewSlotEvent(val id : Int) : Event

object DeleteProjectEvent : Event

data class ConfirmChangesEvent(val project: ProjectModel?) : Event

data class DisplayChangeRolesDialog(val selectedIds : List<Int>) : Event

data class DisplayChangeSpecializationsDialog(val selectedIds: List<Int>) : Event