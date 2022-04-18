package com.example.kusashkotlin.data.repo

import android.util.Log
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.model.Profile
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {
    fun getProfile(username: String) : Single<Profile> {
        return apiHelper.getProfile(username)
    }

    fun getToken(username: String, password: String) : String {
        return apiHelper.getToken(username, password)
    }

    fun getUsernameByToken(token: String) : String {
        return apiHelper.verifyToken(token)
    }

}
