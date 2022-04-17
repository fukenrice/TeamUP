package com.example.kusashkotlin.data.api

import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.kusashkotlin.data.model.Profile
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.example.kusashkotlin.App
import com.example.kusashkotlin.R
import io.reactivex.Single
import org.json.JSONArray

class JS : ParsedRequestListener<Profile> {
    override fun onResponse(response: Profile?) {
        if (response != null) {
            Log.d("mytag", response.age)
        }
    }

    override fun onError(anError: ANError?) {
        if (anError != null) {
            Log.d("mytag", anError.stackTraceToString())
        }
    }

}


class ApiServiceImpl : ApiService {
    override fun getProfile(username: String): Single<Profile> {

        val host = App.getAppResources().getString(R.string.host)
        val protocol = App.getAppResources().getString(R.string.protocol)
        val port = App.getAppResources().getString(R.string.port)



        val profile = Rx2AndroidNetworking.get("${protocol}://${host}:${port}/api/v1/get-profile/${username}/").build().getAsObject(Profile::class.java, JS())
        Log.d("mytag", profile.toString())
        return Rx2AndroidNetworking.get("${protocol}://${host}:${port}/api/v1/get-profile/${username}/").build().getObjectSingle(Profile::class.java)
    }

    override fun getToken(): String {
        TODO("Not yet implemented")
    }
}