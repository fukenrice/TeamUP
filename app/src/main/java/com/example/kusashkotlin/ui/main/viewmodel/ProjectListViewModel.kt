package com.example.kusashkotlin.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError

import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProjectListViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val projects = MutableLiveData<Resource<List<ProjectModel>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchProjects()
    }

    private fun fetchProjects() {
        projects.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getProjects().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    projects.postValue(Resource.success(response))
                }, { throwable ->
                    if (throwable is ANError && throwable.errorCode == 400) {
                        var message: String = ""
                        // TODO: Обработать теги
                        projects.postValue(Resource.error(message, null))
                    } else {
                        projects.postValue(Resource.error("Ошибка", null))
                        Log.d("error", throwable.toString())
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getProjects() : MutableLiveData<Resource<List<ProjectModel>>> {
        return projects
    }
}