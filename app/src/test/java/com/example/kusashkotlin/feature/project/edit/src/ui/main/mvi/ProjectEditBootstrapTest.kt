package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.androidnetworking.error.ANError
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import io.reactivex.Single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.junit.Assert.*

class ProjectEditBootstrapTest {

    private val apiRepo = mock<MainRepository>()

    private val bootstrap = ProjectEditBootstrap(apiRepo)

    @Test
    fun `bootstrap success`() = runTest {
        Mockito.`when`(apiRepo.getProject(any())).thenReturn(Single.just(ProjectModel(id=10)))
        val result = bootstrap.invoke()
        assertEquals(ProjectEditEffect.BootstrapData(ProjectModel(id=10)), result.toList().single())
    }

    @Test
    fun `bootstrap failure`() = runTest {
        Mockito.`when`(apiRepo.getProject(any())).thenReturn(Single.error(ANError("error")))
        val result = bootstrap.invoke()
        assertEquals(ProjectEditEffect.BootstrapData(ProjectModel()), result.toList().single())
    }

}