package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.androidnetworking.error.ANError
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.data.repo.PreferencesRepository
import io.reactivex.Single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.kotlin.any

class ProjectEditActorTest {

    private val apiRepo = mock<MainRepository>()

    private val preferencesRepo = mock<PreferencesRepository>()

    private val actor = ProjectEditActor(apiRepo, preferencesRepo)

    @Test
    fun `when AddSlot action - then AddSlot effect`() = runTest {
        val result = actor.invoke(ProjectEditState.initial, ProjectEditAction.AddSlot).toList()
        assertEquals(ProjectEditEffect.AddSlot, result.single())
    }

    @Test
    fun `when ChangeRoles action - then DisplayRoles effect`() = runTest {
        val state = ProjectEditState.initial
        val result = actor.invoke(state, ProjectEditAction.ChangeRoles).toList()
        assertEquals(ProjectEditEffect.DisplayRoles(state.project.requiredBelbin), result.single())
    }

    @Test
    fun `when ChangeSpecialization action - then DisplaySpec effect`() = runTest {
        val state = ProjectEditState.initial
        val result = actor.invoke(state, ProjectEditAction.ChangeSpecialization).toList()
        assertEquals(
            ProjectEditEffect.DisplaySpec(state.project.requiredSpecialization),
            result.single()
        )
    }

    @Test
    fun `when ConfirmChanges action - then ConfirmChanges effect if success`() = runTest {
        val state = ProjectEditState.initial

        Mockito.`when`(preferencesRepo.getToken()).thenReturn("token")

        Mockito.`when`(apiRepo.updateProject(any(), any())).thenReturn(Single.just("success"))

        val result = actor.invoke(state, ProjectEditAction.ConfirmChanges(state.project))

        assertEquals(
            ProjectEditEffect.ConfirmChanges("Проект успешно изменен", true),
            result.toList().single()
        )
    }

    @Test
    fun `when ConfirmChanges action - then ConfirmChanges effect if fail`() = runTest {
        val state = ProjectEditState.initial

        val errBody = "body"

        val error = ANError("errMsg")

        error.errorBody = errBody

        Mockito.`when`(preferencesRepo.getToken()).thenReturn("token")

        Mockito.`when`(apiRepo.updateProject(any(), any())).thenReturn(Single.error(error))

        val result = actor.invoke(state, ProjectEditAction.ConfirmChanges(state.project))

        assertEquals(
            ProjectEditEffect.ConfirmChanges(errBody, false),
            result.toList().single()
        )
    }

    @Test
    fun `when DeleteProject action - then DeleteProject effect if success`() = runTest {
        val state = ProjectEditState.initial

        Mockito.`when`(preferencesRepo.getToken()).thenReturn("token")

        Mockito.`when`(apiRepo.deleteProject(any())).thenReturn(Single.just("success"))

        val result = actor.invoke(state, ProjectEditAction.DeleteProject)

        assertEquals(
            ProjectEditEffect.DeleteProject("Проект успешно изменен", true),
            result.toList().single()
        )
    }

    @Test
    fun `when DeleteProject action - then DeletePoject effect if fail`() = runTest {
        val state = ProjectEditState.initial

        val errBody = "body"

        val error = ANError("errMsg")

        error.errorBody = errBody

        Mockito.`when`(preferencesRepo.getToken()).thenReturn("token")

        Mockito.`when`(apiRepo.deleteProject(any())).thenReturn(Single.error(error))

        val result = actor.invoke(state, ProjectEditAction.DeleteProject)

        assertEquals(
            ProjectEditEffect.DeleteProject(errBody, false),
            result.toList().single()
        )
    }

    @Test
    fun `when ViewSlot action - then ViewSlot effect`() = runTest {
        val state = ProjectEditState.initial
        val result = actor.invoke(state, ProjectEditAction.ViewSlot(1)).toList()
        assertEquals(
            ProjectEditEffect.ViewSlot(1),
            result.single()
        )
    }

    @Test
    fun `when ConfirmSpecializations action - then ChangeSpecializations effect`() = runTest {
        val state = ProjectEditState.initial
        val result = actor.invoke(state, ProjectEditAction.ConfirmSpecializations(listOf(1,3,4))).toList()
        assertEquals(
            ProjectEditEffect.ChangeSpecializations(listOf(1,3,4)),
            result.single()
        )
    }

    @Test
    fun `when ConfirmRoles action - then ChangeRoles effect`() = runTest {
        val state = ProjectEditState.initial
        val result = actor.invoke(state, ProjectEditAction.ConfirmRoles(listOf(1,3,4))).toList()
        assertEquals(
            ProjectEditEffect.ChangeRoles(listOf(1,3,4)),
            result.single()
        )
    }
}