package com.example.kusashkotlin.data.repo

import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.model.*
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {

    fun getProfile(username: String) : Single<Profile> {
        return apiHelper.getProfile(username)
    }

    fun getToken(username: String, password: String) : Single<TokenResponse> {
        return apiHelper.getToken(username, password)
    }

    fun getUserByToken(token: String) : Single<User> {
        return apiHelper.verifyToken(token)
    }

    fun registerUser(email: String, username: String, password: String) : Single<RegisterResponse> {
        return apiHelper.registerUser(email, username, password)
    }

    fun editProfile(update: ProfileUpdate, token: String) : Single<String> {
        return apiHelper.editProfile(update, token)
    }

    fun sendBelbin(belbinModel: BelbinModel, token: String) : Single<String> {
        return apiHelper.sendBelbin(belbinModel, token)
    }

    fun sendMBTI(mbtiModel: MBTIModel, token: String) : Single<String> {
        return apiHelper.sendMBTI(mbtiModel, token)
    }

    fun updateExecutorOffer(executorOfferSetupModel: ExecutorOfferSetupModel, token: String) : Single<String> {
        return apiHelper.updateExecutorOffer(executorOfferSetupModel, token)
    }

    fun deleteExecutorOffer(token: String) : Single<String> {
        return apiHelper.deleteExecutorOffer(token)
    }

    fun getSpecializations() : Single<List<SpecializationModel>> {
        return apiHelper.getSpecializations()
    }

    fun getBelbilnRoles() : Single<List<BelbinModel>> {
        return apiHelper.getBelbinRoles()
    }
}
