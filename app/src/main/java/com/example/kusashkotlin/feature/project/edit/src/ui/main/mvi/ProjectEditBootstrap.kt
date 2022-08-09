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
import kotlinx.coroutines.rx2.await

object ProjectEditBootstrap : MviBootstrap<ProjectEditEffect> {

    override fun invoke(): Flow<ProjectEditEffect> {
        return flow<ProjectEditEffect> {
            try {
                val response = MainRepository(ApiHelper(ApiServiceImpl()))
                    .getProject("234")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .await()
                emit(ProjectEditEffect.BootstrapData(response))
            } catch(throwable: Throwable) {
                if (throwable is ANError && throwable.errorCode == 404) {
                    ProjectEditEffect.BootstrapData(ProjectModel(id = 322))
                } else {
                    ProjectEditEffect.BootstrapData(ProjectModel(id = 321))
                }
            }
        }
    }
}
