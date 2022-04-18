package com.example.kusashkotlin.data.api

class ApiHelper(private val apiService: ApiService) {
    fun getProfile(username: String) = apiService.getProfile(username)
    fun getToken(login: String, password: String) = apiService.getToken(login, password)
    fun verifyToken(token: String) = apiService.getUsernameByToken(token)
}
