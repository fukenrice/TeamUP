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
    fun getSpecializations() : Single<List<SpecializationModel>>
    fun getBelbinRoles() : Single<List<RoleModel>>
    fun updateProject(projectModel: ProjectModel, token: String) : Single<String>
    fun getProject(title: String) : Single<ProjectModel>
    fun deleteProject(token: String) : Single<String>
    fun getProjects() : Single<List<ProjectModel>>
    fun getExecutorOffers() : Single<List<ExecutorOffer>>
    fun getWorkerSlot(id: Int) : Single<WorkerSlot>
    fun updateWorkerSlot(token: String, workerSlot: WorkerSlot) : Single<String>
    fun inviteProfile(username: String, slotId: Int, token: String) : Single<String>
    fun declineProfile(username: String, slotId: Int, token: String) : Single<String>
    fun deleteWorkerSlot(id: Int, token: String) : Single<String>
    fun getSlotApplies(id: Int, token: String) : Single<List<Profile>>
    fun applyToWorkerSlot(id: Int, token: String) : Single<String>
}
