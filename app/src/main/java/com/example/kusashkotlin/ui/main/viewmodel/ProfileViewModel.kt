package com.example.kusashkotlin.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(private val mainRepository: MainRepository, private val username: String) :
    ViewModel() {
    private val profile = MutableLiveData<Resource<Profile>>()
    private val observerProfile = MutableLiveData<Resource<Profile>>()
    private val compositeDisposable = CompositeDisposable() // TODO: Узнать

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        profile.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getProfile(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profileM ->
                    profile.postValue(Resource.success(profileM))
                }, { throwable ->
                    profile.postValue(Resource.error("Something Went Wrong profile", null))
                })
        )
    }

    fun getOtherProfile(observerUsername: String) {
        profile.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getProfile(observerUsername)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profileM ->
                    observerProfile.postValue(Resource.success(profileM))
                }, { throwable ->
                    observerProfile.postValue(Resource.error("Something Went Wrong profile", null))
                })
        )
    }

    fun getObserverProfile() : LiveData<Resource<Profile>> {
        return observerProfile
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getProfile(): LiveData<Resource<Profile>> {
        return profile
    }
}