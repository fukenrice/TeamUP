package com.example.kusashkotlin.ui.main.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.data.model.WorkerSlot
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RequestedWorkerSlotsViewModel(
    private val mainRepository: MainRepository,
    private val token: String,
    private val context: Context? = null
) :
    ViewModel() {
    private val slots = MutableLiveData<Resource<List<WorkerSlot>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchWorkerSlots()
    }

    private fun fetchWorkerSlots() {
        slots.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getRequestedSlots(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    slots.postValue(Resource.success(response))
                }, { throwable ->
                    slots.postValue(Resource.error("Something Went Wrong worker slots", null))
                })
        )
    }

    fun retractApply(id: Int) {
        compositeDisposable.add(
            mainRepository.retractRequest(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()
                }, { throwable ->
                    if (throwable is ANError) {
                        Toast.makeText(context, throwable.errorBody, Toast.LENGTH_SHORT).show()
                    }
                })
        )
    }

    fun getSlots(): LiveData<Resource<List<WorkerSlot>>> {
        return slots
    }

}