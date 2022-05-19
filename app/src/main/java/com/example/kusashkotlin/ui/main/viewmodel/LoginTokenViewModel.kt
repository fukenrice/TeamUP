package com.example.kusashkotlin.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.api.ApiServiceImpl
import com.example.kusashkotlin.data.model.TokenResponse
import com.example.kusashkotlin.data.model.User
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginTokenViewModel(
    private val mainRepository: MainRepository,
    private val username: String = "",
    private val password: String = "",

    ) : ViewModel() {
    private var tokenLiveData = MutableLiveData<Resource<TokenResponse>>()
    private val compositeDisposable = CompositeDisposable()


    init {
        fetchToken()
    }

    private fun fetchToken() {
        tokenLiveData.postValue(Resource.loading(null))

        compositeDisposable.add(
            mainRepository.getToken(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    tokenLiveData.postValue(Resource.success(response))
                }, { throwable ->
                    if (throwable is ANError && throwable.errorCode == 400) {
                        tokenLiveData.postValue(
                            Resource.error(
                                JSONObject(throwable.errorBody).getJSONArray(
                                    "non_field_errors"
                                )[0].toString(), null
                            )
                        )
                    } else {
                        tokenLiveData.postValue(Resource.error("Ошибка авторизации", null))
                        Log.d("mytag", throwable.toString())
                    }
                })
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getToken(): LiveData<Resource<TokenResponse>> {
        return tokenLiveData
    }
}
