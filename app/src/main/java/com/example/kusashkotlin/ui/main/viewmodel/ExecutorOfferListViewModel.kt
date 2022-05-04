package com.example.kusashkotlin.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError

import com.example.kusashkotlin.data.model.ExecutorOffer
import com.example.kusashkotlin.data.model.ProjectModel
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ExecutorOfferListViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val offers = MutableLiveData<Resource<List<ExecutorOffer>>>()

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchOffers()
    }

    private fun fetchOffers() {
        offers.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getExecutorOffers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    offers.postValue(Resource.success(response))
                }, { throwable ->
                    if (throwable is ANError && throwable.errorCode == 400) {
                        var message: String = ""
                        // TODO: Обработать теги
                        offers.postValue(Resource.error(message, null))
                    } else {
                        offers.postValue(Resource.error("Ошибка", null))
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getOffers() : MutableLiveData<Resource<List<ExecutorOffer>>> {
        return offers
    }


}