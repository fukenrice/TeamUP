package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.arrival.mvi.MviReducer

object ProjectEditReducer : MviReducer<ProjectEditEffect, ProjectEditState> {
    override suspend fun invoke(
        previousState: ProjectEditState,
        effect: ProjectEditEffect
    ): ProjectEditState {
        return when (effect) {
            is ProjectEditEffect.BootstrapData -> if (effect.project != null) {
                previousState.copy(project = effect.project)
            } else {
                previousState
            }
            is ProjectEditEffect.AddSlot -> previousState // Event, открыть новую активити, потом надо заново получить проджект, чтобы обновить слоты(или есть вариант лучше?)
            is ProjectEditEffect.DeleteProject -> previousState // Event удаления
            is ProjectEditEffect.DisplayRoles -> previousState // Event открыть диалог
            is ProjectEditEffect.DisplaySpec -> previousState // Event открыть диалог
            is ProjectEditEffect.ViewSlot -> previousState // Event, открыть активити редактирования слота, потом заново получить проект.
            is ProjectEditEffect.ConfirmChanges -> previousState // Event, отправить из текущего стейта проект.
            is ProjectEditEffect.ChangeRoles -> previousState.copy(project = previousState.project.copy(requiredBelbin = effect.selectedIds)) // Event, открыть диалог с выбранными ролями
            is ProjectEditEffect.ChangeSpecializations -> previousState.copy(project = previousState.project.copy(requiredSpecialization = effect.selectedIds))
        }
    }
}