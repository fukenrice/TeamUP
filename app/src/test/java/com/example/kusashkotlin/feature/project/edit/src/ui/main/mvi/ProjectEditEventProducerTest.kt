package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class ProjectEditEventProducerTest {

    private val producer = ProjectEditEventProducer

    @Test
    fun `when AddSlot effect - then AddSlot event`() = runTest {
        val result = producer.invoke(ProjectEditEffect.AddSlot)
        assertEquals(AddSlotEvent, result)
    }

    @Test
    fun `when ViewSlot effect - then ViewSlot event`() = runTest {
        val result = producer.invoke(ProjectEditEffect.ViewSlot(1))
        assertEquals(ViewSlotEvent(1), result)
    }

    @Test
    fun `when DeleteProject effect - then DeleteProject event`() = runTest {
        val result = producer.invoke(ProjectEditEffect.DeleteProject("success", true))
        assertEquals(DeleteProjectEvent("success", true), result)
    }

    @Test
    fun `when ConfirmChanges effect - then ConfirmChanges event`() = runTest {
        val result = producer.invoke(ProjectEditEffect.ConfirmChanges("success", true))
        assertEquals(ConfirmChangesEvent("success", true), result)
    }

    @Test
    fun `when DisplayRoles effect - then DisplayChangeRolesDialog event`() = runTest {
        val result = producer.invoke(ProjectEditEffect.DisplayRoles(listOf(1,2,3)))
        assertEquals(DisplayChangeRolesDialog(listOf(1,2,3)), result)
    }

    @Test
    fun `when DisplaySpec effect - then DisplayChangeSpecializationsDialog event`() = runTest {
        val result = producer.invoke(ProjectEditEffect.DisplaySpec(listOf(1,2,3)))
        assertEquals(DisplayChangeSpecializationsDialog(listOf(1,2,3)), result)
    }
}
