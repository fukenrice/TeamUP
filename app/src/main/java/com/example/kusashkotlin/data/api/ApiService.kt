package com.example.kusashkotlin.data.api

import com.example.kusashkotlin.data.model.Profile
import com.example.kusashkotlin.data.model.RegisterResponse
import com.example.kusashkotlin.data.model.TokenResponse
import com.example.kusashkotlin.data.model.User
import io.reactivex.Single

interface ApiService {
    fun getProfile(username: String): Single<Profile>
    fun getToken(login: String, password: String): Single<TokenResponse>
    fun getUserByToken(token: String): Single<User>
    fun registerUser(email: String, username: String, password: String): Single<RegisterResponse>
}
