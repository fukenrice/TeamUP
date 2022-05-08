package com.example.kusashkotlin.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.model.WorkerSlot
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WorkerSlotViewModel(private val mainRepository: MainRepository, private val id: Int) : ViewModel() {
    private val slot = MutableLiveData<Resource<WorkerSlot>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchWorkerSlot()
    }

    private fun fetchWorkerSlot() {
        slot.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getWorkerSlot(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    slot.postValue(Resource.success(response))
                }, { throwable ->
                    slot.postValue(Resource.error("Something Went Wrong worker slot", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getWorkerSlot() : LiveData<Resource<WorkerSlot>> {
        return slot
    }

}