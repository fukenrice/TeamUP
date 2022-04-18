package com.example.kusashkotlin.data.api

import com.example.kusashkotlin.data.model.Profile
import io.reactivex.Single

interface ApiService {
    fun getProfile(username: String): Single<Profile>
    fun getToken(login: String, password: String): String
    fun getUsernameByToken(token: String): String
}
