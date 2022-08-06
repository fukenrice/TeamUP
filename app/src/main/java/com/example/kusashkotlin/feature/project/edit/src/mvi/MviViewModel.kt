package com.example.kusashkotlin.feature.project.edit.src.mvi

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arrival.mvi.MviFeatureFactory
import com.arrival.mvi.utils.Notification
import com.arrival.mvi.utils.dematerialize
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class MviViewModel<State : Any, Action : Any>(
    factory: MviFeatureFactory<Action, *, State, Event>
) : ViewModel() {

    companion object {
        fun <State> Flow<*>.processFeature(
            onEvent: (Event) -> Unit,
            errorEventProducer: (Throwable) -> Event?
        ) = transform<Any?, Notification<State>> {
            when (it) {
                is Event -> flatEvent(it, errorEventProducer).forEach(onEvent)
                is Notification.OnError<*> -> errorEventProducer(it.error)?.let(onEvent)
                is Notification.OnNext<*> -> emit(it as Notification.OnNext<State>)
            }
        }.dematerialize() // ??

        private fun flatEvent(event: Event, errorEventProducer: (Throwable) -> Event?): List<Event> {
            return when (event) {
                is CompositeEvent -> event.events.flatMap { flatEvent(it, errorEventProducer) }
                is ErrorEvent -> listOfNotNull(errorEventProducer(event.throwable))
                else -> listOf(event)
            }
        }
    }

    private val feature = factory.create(viewModelScope)

    private var featureStateFlow: StateFlow<State>? = null

    fun acceptAction(action: Action) {
        viewModelScope.launch { feature.accept(action) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observe(onEvent: (Event) -> Unit): Flow<State> =
        merge(feature, feature.events).processFeature(onEvent, ::produceErrorEvent)

    private fun Flow<State>.asStateFlow(sharingStrategy: SharingStarted = SharingStarted.Lazily): StateFlow<State> {
        return stateIn(viewModelScope, sharingStrategy, feature.state)
    }

    private fun produceErrorEvent(throwable: Throwable): Event = ErrorEvent(throwable)

    private fun asCachedStateFlow(onEvent: (Event) -> Unit): Flow<State> {
        return featureStateFlow.takeIf { it != null }
            ?: let {
                observe(onEvent)
                    .asStateFlow()
                    .also {
                        featureStateFlow = it
                    }
            }
    }

    fun observe(lifecycleScope: LifecycleCoroutineScope, eventsFun: (Event) -> Unit, renderFun: (State) -> Unit) {
        lifecycleScope.launchWhenCreated {
            asCachedStateFlow(eventsFun).collect { renderFun.invoke(it) }
        }
    }
}