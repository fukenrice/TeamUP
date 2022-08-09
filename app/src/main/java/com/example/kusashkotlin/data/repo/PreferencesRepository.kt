package com.example.kusashkotlin.data.repo

import androidx.appcompat.app.AppCompatActivity
import com.example.kusashkotlin.App
import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel
import com.google.gson.Gson

class PreferencesRepository {

    val prefs = App.getContext().getSharedPreferences("APP", AppCompatActivity.MODE_PRIVATE)

    fun getToken() : String {
        return prefs.getString("token", "").toString()
    }

    fun getAllRoles() : List<RoleModel>? {
        if (prefs.contains("roles")) {
            return Gson().fromJson(prefs.getString("roles", ""), Array<RoleModel>::class.java).toList()
        }
        return null
    }

    fun getAllSpecializations() : List<SpecializationModel>? {
        if (prefs.contains("specializations")) {
            return Gson().fromJson(prefs.getString("specializations", ""), Array<SpecializationModel>::class.java).toList()
        }
        return null
    }
}
