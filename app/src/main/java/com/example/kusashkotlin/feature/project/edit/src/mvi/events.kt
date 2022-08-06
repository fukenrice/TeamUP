package com.example.kusashkotlin.feature.project.edit.src.mvi

interface Event

data class ErrorEvent(val throwable: Throwable, val message: String? = throwable.message) : Event

data class MessageEvent(val message: String) : Event

data class CompositeEvent(val events: List<Event>) : Event
