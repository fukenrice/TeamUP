package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import com.androidnetworking.error.ANError
import com.arrival.mvi.MviBootstrap
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object ProjectEditBootstrap : MviBootstrap<ProjectEditEffect> {

    override fun invoke(): Flow<ProjectEditEffect> {


        val response: Single<ProjectModel> =
            MainRepository(ApiHelper(ApiServiceImpl())).getProject("") // TODO: Доставать из репозитория title текущего проекта


        return flow {
            response.subscribe({ response ->
                GlobalScope.launch { emit(ProjectEditEffect.BootstrapData(response)) } // Не из контекста корутины нельзя эмитить
            }, { throwable ->
                if (throwable is ANError && throwable.errorCode == 404) {
                    GlobalScope.launch { emit(ProjectEditEffect.BootstrapData(ProjectModel())) }
                } else {
                    // TODO: Как правильно обрабатывать ошибки из-за плохого соединения и тд?
                }
            })
        }
    }
}
