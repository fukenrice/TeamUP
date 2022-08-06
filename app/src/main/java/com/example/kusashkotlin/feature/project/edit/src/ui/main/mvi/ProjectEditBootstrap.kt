package com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi

import android.util.Log
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

object ProjectEditBootstrap : MviBootstrap<ProjectEditEffect> {

    override fun invoke(): Flow<ProjectEditEffect> {


        val response: Single<ProjectModel> =
            MainRepository(ApiHelper(ApiServiceImpl())).getProject("234") // TODO: Из Intent получать название проекта и передавать его в конструктор вью модели

        val f = MutableSharedFlow<ProjectEditEffect>()

        response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                GlobalScope.launch { f.emit(ProjectEditEffect.BootstrapData(response)) } // Не из контекста корутины нельзя эмитить
            }, { throwable ->
                if (throwable is ANError && throwable.errorCode == 404) {
                    GlobalScope.launch { f.emit(ProjectEditEffect.BootstrapData(ProjectModel(id = 322))) }
                } else {
                    // TODO: Как правильно обрабатывать ошибки из-за плохого соединения и тд?
                    GlobalScope.launch { f.emit(ProjectEditEffect.BootstrapData(ProjectModel(id = 321))) }
                }
            })

        return f
    }
}
