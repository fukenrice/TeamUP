package com.example.kusashkotlin.data.api

import com.example.kusashkotlin.data.model.BelbinModel
import com.example.kusashkotlin.data.model.ProfileUpdate

class ApiHelper(private val apiService: ApiService) {
    fun getProfile(username: String) = apiService.getProfile(username)
    fun getToken(login: String, password: String) = apiService.getToken(login, password)
    fun verifyToken(token: String) = apiService.getUserByToken(token)
    fun registerUser(email: String, username: String, password: String) =
        apiService.registerUser(email, username, password)

    fun editProfile(update: ProfileUpdate, token: String) = apiService.editProfile(update, token)
    fun sendBelbin(belbinModel: BelbinModel, token: String) =
        apiService.sendBelbin(belbinModel, token)
}
