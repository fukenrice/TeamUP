package com.example.kusashkotlin.data.api

import com.example.kusashkotlin.data.model.*

class ApiHelper(private val apiService: ApiService) {
    fun getProfile(username: String) = apiService.getProfile(username)
    fun getToken(login: String, password: String) = apiService.getToken(login, password)
    fun verifyToken(token: String) = apiService.getUserByToken(token)
    fun registerUser(email: String, username: String, password: String) =
        apiService.registerUser(email, username, password)

    fun editProfile(update: ProfileUpdate, token: String) = apiService.editProfile(update, token)
    fun sendBelbin(belbinModel: BelbinModel, token: String) =
        apiService.sendBelbin(belbinModel, token)

    fun sendMBTI(mbtiModel: MBTIModel, token: String) = apiService.sendMBTI(mbtiModel, token)
    fun updateExecutorOffer(executorOfferSetupModel: ExecutorOfferSetupModel, token: String) =
        apiService.updateExecutorOffer(executorOfferSetupModel, token)

    fun deleteExecutorOffer(token: String) = apiService.deleteExecutorOffer(token)
    fun getSpecializations() = apiService.getSpecializations()
    fun getBelbinRoles() = apiService.getBelbinRoles()
    fun updateProject(projectModel: ProjectModel, token: String) =
        apiService.updateProject(projectModel, token)

    fun getProject(title: String) = apiService.getProject(title)
    fun deleteProject(token: String) = apiService.deleteProject(token)
    fun getProjects() = apiService.getProjects()
    fun getExecutorOffers() = apiService.getExecutorOffers()
    fun getWorkerSlot(id: Int) = apiService.getWorkerSlot(id)
    fun updateWorkerSlot(id: Int, token: String, workerSlot: WorkerSlot) =
        apiService.updateWorkerSlot(id, token, workerSlot)

    fun inviteProfile(username: String, slotId: Int, token: String) =
        apiService.inviteProfile(username, slotId, token)

    fun declineProfile(username: String, slotId: Int, token: String) =
        apiService.declineProfile(username, slotId, token)

    fun deleteWorkerSlot(id: Int, token: String) = apiService.deleteWorkerSlot(id, token)
}
