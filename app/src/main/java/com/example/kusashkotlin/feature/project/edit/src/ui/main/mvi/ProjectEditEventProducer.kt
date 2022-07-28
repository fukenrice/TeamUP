package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.arrival.mvi.EventProducer
import com.example.kusashkotlin.feature.project.edit.src.mvi.Event

object ProjectEditEventProducer : EventProducer<ProjectEditEffect, Event> {
    override fun invoke(effect: ProjectEditEffect): Event? {
        return when (effect) {
            is ProjectEditEffect.AddSlot -> AddSlotEvent
            is ProjectEditEffect.ViewSlot -> ViewSlotEvent(effect.id)
            is ProjectEditEffect.DeleteProject -> DeleteProjectEvent
            is ProjectEditEffect.ConfirmChanges -> ConfirmChangesEvent(effect.project)
            is ProjectEditEffect.ChangeRoles -> DisplayChangeRolesDialog(effect.selectedIds)
            is ProjectEditEffect.ChangeSpecializations -> DisplayChangeSpecializationsDialog(effect.selectedIds)
            else -> null
        }
    }
}