package com.example.kusashkotlin.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kusashkotlin.data.model.User
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginUserViewModel(
    private val mainRepository: MainRepository,
    private val token: String = ""
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val userLiveData = MutableLiveData<Resource<User>>()

    init {
        fetchUser()
    }

    private fun fetchUser() {
        userLiveData.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getUserByToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    userLiveData.postValue(Resource.success(response))
                }, { throwable ->
                    userLiveData.postValue(Resource.error("Ошибка получения пользователя", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getUser(): LiveData<Resource<User>> {
        return userLiveData
    }
}
