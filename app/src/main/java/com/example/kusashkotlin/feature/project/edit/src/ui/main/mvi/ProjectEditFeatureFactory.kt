package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.arrival.mvi.MviFeatureFactory
import com.example.kusashkotlin.feature.project.edit.src.mvi.Event

class ProjectEditFeatureFactory(actor: ProjectEditActor, bootstrap: ProjectEditBootstrap) :
    MviFeatureFactory<ProjectEditAction, ProjectEditEffect, ProjectEditState, Event>(
        initialState = ProjectEditState.initial,
        bootstrap = bootstrap,
        actor = actor,
        eventProducer = ProjectEditEventProducer,
        reducer = ProjectEditReducer
    )