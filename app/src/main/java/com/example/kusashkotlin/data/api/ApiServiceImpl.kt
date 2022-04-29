package com.example.kusashkotlin.data.api

import android.util.Log
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.example.kusashkotlin.App
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import io.reactivex.Single
import org.json.JSONObject
import org.json.JSONStringer

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
        return Rx2AndroidNetworking.post("${url}/auth/token/login/")
            .addBodyParameter("username", login)
            .addBodyParameter("password", password)
            .build()
            .getObjectSingle(TokenResponse::class.java)
    }

    override fun getUserByToken(token: String): Single<User> {
        return Rx2AndroidNetworking.get("${url}/auth/users/me/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(User::class.java)
    }

    override fun registerUser(
        email: String,
        username: String,
        password: String
    ): Single<RegisterResponse> {
        return Rx2AndroidNetworking.post("${url}/auth/users/")
            .addBodyParameter("email", email)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .build()
            .getObjectSingle(RegisterResponse::class.java)
    }

    override fun editProfile(update: ProfileUpdate, token: String): Single<String> {
        return Rx2AndroidNetworking.patch("${url}/api/v1/edit-profile/")
            .addJSONObjectBody(JSONObject(Gson().toJson(update)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun sendBelbin(belbinModel: BelbinModel, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/process-belbin/")
            .addJSONObjectBody(JSONObject(Gson().toJson(belbinModel)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)

    }

    override fun sendMBTI(mbtiModel: MBTIModel, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/process-mbti/")
            .addJSONObjectBody(JSONObject(Gson().toJson(mbtiModel)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }
}
