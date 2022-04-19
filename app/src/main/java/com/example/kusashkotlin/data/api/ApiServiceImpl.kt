package com.example.kusashkotlin.data.api

import android.util.Log
import com.example.kusashkotlin.data.model.Profile
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.example.kusashkotlin.App
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.TokenResponse
import com.example.kusashkotlin.data.model.User
import io.reactivex.Single

class ApiServiceImpl : ApiService {
    private val host = App.getAppResources().getString(R.string.host)
    private val protocol = App.getAppResources().getString(R.string.protocol)
    private val port = App.getAppResources().getString(R.string.port)
    private val url = "${protocol}://${host}:${port}"



    override fun getProfile(username: String): Single<Profile> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-profile/${username}/")
            .build()
            .getObjectSingle(Profile::class.java)
    }

    override fun getToken(login: String, password: String): Single<TokenResponse> {
        Log.d("mytagApi", login + " " + password)
        return Rx2AndroidNetworking.post("${url}/auth/token/login")
            .addBodyParameter("username", login)
            .addBodyParameter("password", password)
            .build()
            .getObjectSingle(TokenResponse::class.java)
    }

    override fun getUserByToken(token: String): Single<User> {
        var username = ""
        return Rx2AndroidNetworking.get("${url}/auth/users/me")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(User::class.java)
    }
}
