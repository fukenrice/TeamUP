package com.example.kusashkotlin.data.api

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.kusashkotlin.data.model.Profile
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.example.kusashkotlin.App
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.TokenResponse
import io.reactivex.Single
import org.json.JSONObject

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

    override fun getToken(login: String, password: String): String {
        val token = arrayOf("")
        Log.d("login getting token data", "login " + login + " password " + password)
        val qwe = AndroidNetworking.post("${url}/auth/token/login")
            .addBodyParameter("username", login)
            .addBodyParameter("password", password)
            .build()
            .getAsObject(TokenResponse::class.java, object: ParsedRequestListener<TokenResponse> {
                override fun onResponse(response: TokenResponse?) {
                    TODO("Not yet implemented")
                }

                override fun onError(anError: ANError?) {
                    TODO("Not yet implemented")
                }

            })
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject?) {
//                    token[0] = response?.get("auth_token").toString()
//                }
//
//                override fun onError(anError: ANError?) {
//                    token[0] = "error"
//                    if (anError != null) {
//                        Log.d("login getting token error", anError.errorBody)
//                    }
//                }
//            })

        Log.d("login getting token", token[0])
        return qwe
    }

    override fun getUsernameByToken(token: String): String {
        var username = ""
        Rx2AndroidNetworking.get("${url}/auth/users/me")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response != null) {
                        username = response.get("username").toString()
                    }
                }

                override fun onError(anError: ANError?) {
                    if (anError != null) {
                        Log.d("mytag", "error token " + anError.errorBody.toString())
                    }
                }
            })
        return username
    }
}