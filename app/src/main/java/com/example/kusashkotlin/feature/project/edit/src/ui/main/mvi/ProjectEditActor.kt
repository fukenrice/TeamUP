package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.arrival.mvi.MviActor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object ProjectEditActor : MviActor<ProjectEditAction, ProjectEditEffect, ProjectEditState> {
    override fun invoke(
        previousState: ProjectEditState,
        action: ProjectEditAction
    ): Flow<ProjectEditEffect> {
        return when (action) {
            is ProjectEditAction.AddSlot -> flowOf(ProjectEditEffect.AddSlot)
            is ProjectEditAction.ChangeRoles -> flowOf(ProjectEditEffect.ChangeRoles(previousState.project.requiredBelbin))
            is ProjectEditAction.ChangeSpecialization -> flowOf(ProjectEditEffect.ChangeSpecializations(previousState.project.requiredSpecialization))
            is ProjectEditAction.ConfirmChanges -> flowOf(ProjectEditEffect.ConfirmChanges(previousState.project))
            is ProjectEditAction.DeleteProject -> flowOf(ProjectEditEffect.DeleteProject)
            is ProjectEditAction.ViewSlot -> flowOf(ProjectEditEffect.ViewSlot(id = action.id))
        }
    }
}