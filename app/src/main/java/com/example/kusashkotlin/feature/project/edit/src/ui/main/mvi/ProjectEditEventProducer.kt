package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.arrival.mvi.EventProducer
import com.example.kusashkotlin.feature.project.edit.src.mvi.Event

object ProjectEditEventProducer : EventProducer<ProjectEditEffect, Event> {
    override fun invoke(effect: ProjectEditEffect): Event? {
        return when (effect) {
            is ProjectEditEffect.AddSlot -> AddSlotEvent
            is ProjectEditEffect.ViewSlot -> ViewSlotEvent(effect.id)
            is ProjectEditEffect.DeleteProject -> DeleteProjectEvent(effect.message, effect.success)
            is ProjectEditEffect.ConfirmChanges -> ConfirmChangesEvent(effect.message, effect.success)
            is ProjectEditEffect.DisplayRoles -> DisplayChangeRolesDialog(effect.roles)
            is ProjectEditEffect.DisplaySpec -> DisplayChangeSpecializationsDialog(effect.specializations)
            else -> null
        }
    }
}