package com.example.kusashkotlin.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.data.model.RegisterResponse
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject

class RegisterViewModel(
    private val mainRepository: MainRepository,
    private val email: String,
    private val username: String,
    private val password: String,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val responseLiveData = MutableLiveData<Resource<RegisterResponse>>()

    private fun register() {
        responseLiveData.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.registerUser(email, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    responseLiveData.postValue(Resource.success(response))
                }, { throwable ->
                    if (throwable is ANError && throwable.errorCode == 400) {
                        var message = ""
                        val response = JSONObject(throwable.errorBody)
                        Log.d("error", response.toString())
                        if (response.has("email")) {
                            val arr = response.getJSONArray("email")
                            for (i in 0 until arr.length()) {
                                message = message + arr[i] + "\n"
                            }
                        }
                        if (response.has("username")) {
                            val arr = response.getJSONArray("username")
                            for (i in 0 until arr.length()) {
                                message = message + arr[i] + "\n"
                            }
                        }
                        if (response.has("password")) {
                            val arr = response.getJSONArray("password")
                            for (i in 0 until arr.length()) {
                                message = message + arr[i] + "\n"
                            }
                        }
                        responseLiveData.postValue(Resource.error(message, null))
                    } else {
                        responseLiveData.postValue(Resource.error("Ошибка регистрации", null))
                    }
                })
        )
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun registerUser() : LiveData<Resource<RegisterResponse>> {
        register()
        return responseLiveData
    }
}
