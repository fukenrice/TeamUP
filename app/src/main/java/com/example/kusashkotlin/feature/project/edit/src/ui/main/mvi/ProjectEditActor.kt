package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.androidnetworking.error.ANError
import com.arrival.mvi.MviActor
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.data.repo.PreferencesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await

class ProjectEditActor(
    private val repository: MainRepository,
    private val preferencesRepository: PreferencesRepository
) : MviActor<ProjectEditAction, ProjectEditEffect, ProjectEditState> {
    override fun invoke(
        previousState: ProjectEditState,
        action: ProjectEditAction
    ): Flow<ProjectEditEffect> {
        // Репозитории через конструктор или так инициализировать, в чем разница?
        // Если контекст репозитирий берет сам, то разницы нет?
        return when (action) {
            is ProjectEditAction.AddSlot -> flowOf(ProjectEditEffect.AddSlot)
            is ProjectEditAction.ChangeRoles -> flowOf(ProjectEditEffect.DisplayRoles(previousState.project.requiredBelbin))
            is ProjectEditAction.ChangeSpecialization -> flowOf(
                ProjectEditEffect.DisplaySpec(
                    previousState.project.requiredSpecialization
                )
            )
            is ProjectEditAction.ConfirmChanges -> {
                return flow {
                    try {
                        val response = repository.updateProject(
                            previousState.project.copy(
                                title = action.project.title,
                                description = action.project.description,
                                vacant = action.project.vacant,
                                city = action.project.city,
                                online = action.project.online
                            ),
                            preferencesRepository.getToken()
                        )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .await()
                        emit(
                            ProjectEditEffect.ConfirmChanges(
                                "Проект успешно изменен", true
                            )
                        )
                    } catch (throwable: Throwable) {
                        if (throwable is ANError) {
                            emit(
                                ProjectEditEffect.ConfirmChanges(
                                    throwable.errorBody, false
                                )
                            )
                        } else {
                            emit(
                                ProjectEditEffect.ConfirmChanges(
                                    "Что-то пошло не так", false
                                )
                            )
                        }
                    }
                }
            }

            is ProjectEditAction.DeleteProject -> {
                return flow {
                    try {
                        val response = repository.deleteProject(preferencesRepository.getToken())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .await()
                        emit(
                            ProjectEditEffect.DeleteProject(
                                "Проект успешно изменен", true
                            )
                        )
                    } catch (throwable: Throwable) {
                        if (throwable is ANError) {
                            emit(
                                ProjectEditEffect.DeleteProject(
                                    throwable.errorBody, false
                                )
                            )
                        } else {
                            emit(
                                ProjectEditEffect.DeleteProject(
                                    "Что-то пошло не так", false
                                )
                            )
                        }
                    }
                }
            }
            is ProjectEditAction.ViewSlot -> flowOf(ProjectEditEffect.ViewSlot(id = action.id))
            is ProjectEditAction.ConfirmSpecializations -> flowOf(
                ProjectEditEffect.ChangeSpecializations(
                    action.newSpecializations
                )
            )
            is ProjectEditAction.ConfirmRoles -> flowOf(ProjectEditEffect.ChangeRoles(action.newRoles))
        }
    }
}