package com.example.kusashkotlin.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProjectEditViewModel(private val mainRepository: MainRepository, private val title: String) : ViewModel() {

    private val project = MutableLiveData<Resource<ProjectModel>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchProject()
    }

    private fun fetchProject() {
        project.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getProject(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    project.postValue(Resource.success(response))
                }, { throwable ->
                    project.postValue(Resource.error("Something Went Wrong profile", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getProject() : LiveData<Resource<ProjectModel>> {
        return project
    }
}