package com.example.kusashkotlin.data.repo

import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.model.TokenResponse
import com.example.kusashkotlin.data.model.User
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {
    fun getProfile(username: String) : Single<Profile> {
        return apiHelper.getProfile(username)
    }

    fun getToken(username: String, password: String) : Single<TokenResponse> {
        return apiHelper.getToken(username, password)
    }

    fun getUserByToken(token: String) : Single<User> {
        return apiHelper.verifyToken(token)
    }
}
