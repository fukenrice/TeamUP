package com.example.kusashkotlin.data.api

class ApiHelper(private val apiService: ApiService) {
    fun getProfile(username: String) = apiService.getProfile(username)
    fun getToken() = apiService.getToken()
}
