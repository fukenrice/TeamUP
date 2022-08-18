package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.example.kusashkotlin.data.model.ProjectModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class ProjectEditReducerTest {

    private val reducer = ProjectEditReducer

    @Test
    fun `when BootstrapData effect - then change state`() = runTest {
        val state = ProjectEditState.initial
        val result = reducer.invoke(state, ProjectEditEffect.BootstrapData(ProjectModel(id = 1)))
        assertEquals(ProjectEditState(ProjectModel(id = 1)), result)
    }

    @Test
    fun `when ChangeRoles effect - then change state`() = runTest {
        val state = ProjectEditState.initial
        val result = reducer.invoke(state, ProjectEditEffect.ChangeRoles(listOf(1,2,3)))
        assertEquals(ProjectEditState(ProjectModel(requiredBelbin = listOf(1,2,3))), result)
    }

    @Test
    fun `when ChangeSpecializations effect - then change state`() = runTest {
        val state = ProjectEditState.initial
        val result = reducer.invoke(state, ProjectEditEffect.ChangeSpecializations(listOf(1,2,3)))
        assertEquals(ProjectEditState(ProjectModel(requiredSpecialization = listOf(1,2,3))), result)
    }

}