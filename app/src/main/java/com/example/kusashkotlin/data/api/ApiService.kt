package com.example.kusashkotlin.data.api

import com.example.kusashkotlin.data.model.*
import io.reactivex.Single

interface ApiService {
    fun getProfile(username: String): Single<Profile>
    fun getToken(login: String, password: String): Single<TokenResponse>
    fun getUserByToken(token: String): Single<User>
    fun registerUser(email: String, username: String, password: String): Single<RegisterResponse>
    fun editProfile(update: ProfileUpdate, token: String): Single<String>
    fun sendBelbin(belbinModel: BelbinModel, token: String) : Single<String>
    fun sendMBTI(mbtiModel: MBTIModel, token: String) : Single<String>
    fun updateExecutorOffer(executorOfferSetupModel: ExecutorOfferSetupModel, token: String) : Single<String>
    fun deleteExecutorOffer(token: String) : Single<String>


}
