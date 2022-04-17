package com.example.kusashkotlin.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(private val mainRepository: MainRepository) : ViewModel() {
    public val profile = MutableLiveData<Resource<Profile>>()
    private val compositeDisposable = CompositeDisposable() // Узнать

    init {

        fetchProfile()
    }

    private fun fetchProfile() {
        profile.postValue(Resource.loading(null))
//        compositeDisposable.add(
//            mainRepository.getProfile("test") //Узнать
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ profileM ->
//                    profile.postValue(Resource.success(profileM))
//                }, { throwable ->
//                    profile.postValue(Resource.error("Something Went Wrong", null))
//
//                    Log.d("mytag", throwable.stackTraceToString())
//                })
//        )

        val gfgThread = Thread {
            try {
                val pr = mainRepository.getProfile("test")
                val a = pr.subscribe({p -> p.photo})
                Log.d("mytag", a.toString())
            } catch (e: java.lang.Exception) {
                throw e
            }
        }

        gfgThread.start()



    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getProfile(): LiveData<Resource<Profile>> {
        return profile
    }
}