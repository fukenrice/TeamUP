package com.example.kusashkotlin.feature.project.edit.src.mvi

import androidx.lifecycle.ViewModel
import com.arrival.mvi.MviFeatureFactory

abstract class MviViewModel<State : Any, Action : Any>(
    factory: MviFeatureFactory<Action, *, State, Event>
) : ViewModel() {


}