package com.example.kusashkotlin.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.repo.MainRepository
import com.example.kusashkotlin.ui.main.viewmodel.ProfileViewModel

class ViewModelFactory(private val apiHelper: ApiHelper, private val username: String, private val password: String = "") : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(MainRepository(apiHelper), username) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
