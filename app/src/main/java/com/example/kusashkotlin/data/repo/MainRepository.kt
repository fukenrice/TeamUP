package com.example.kusashkotlin.data.repo

import com.example.kusashkotlin.data.api.ApiHelper
import com.example.kusashkotlin.data.model.*
import io.reactivex.Single

// Опять все в одну кучу и снова просто заглушка без какой то логики.
// ApiService -> ApiHelper -> MainRepository - в такой релизации двае последних сущности не имеют смысла
class MainRepository(private val apiHelper: ApiHelper) {

    fun getProfile(username: String): Single<Profile> {
        return apiHelper.getProfile(username)
    }

    fun getToken(username: String, password: String): Single<TokenResponse> {
        return apiHelper.getToken(username, password)
    }

    fun getUserByToken(token: String): Single<User> {
        return apiHelper.verifyToken(token)
    }

    fun registerUser(email: String, username: String, password: String): Single<RegisterResponse> {
        return apiHelper.registerUser(email, username, password)
    }

    fun editProfile(update: ProfileUpdate, token: String): Single<String> {
        return apiHelper.editProfile(update, token)
    }

    fun sendBelbin(belbinModel: BelbinModel, token: String): Single<List<String>> {
        return apiHelper.sendBelbin(belbinModel, token)
    }

    fun sendMBTI(mbtiModel: MBTIModel, token: String): Single<List<String>> {
        return apiHelper.sendMBTI(mbtiModel, token)
    }

    fun updateExecutorOffer(
        executorOfferSetupModel: ExecutorOfferSetupModel,
        token: String
    ): Single<String> {
        return apiHelper.updateExecutorOffer(executorOfferSetupModel, token)
    }

    fun deleteExecutorOffer(token: String): Single<String> {
        return apiHelper.deleteExecutorOffer(token)
    }

    fun getSpecializations(): Single<List<SpecializationModel>> {
        return apiHelper.getSpecializations()
    }

    fun getBelbilnRoles(): Single<List<RoleModel>> {
        return apiHelper.getBelbinRoles()
    }

    fun updateProject(projectModel: ProjectModel, token: String): Single<String> {
        return apiHelper.updateProject(projectModel, token)
    }

    fun getProject(title: String): Single<ProjectModel> {
        return apiHelper.getProject(title)
    }

    fun deleteProject(token: String): Single<String> {
        return apiHelper.deleteProject(token)
    }

    fun getProjects(): Single<List<ProjectModel>> {
        return apiHelper.getProjects()
    }

    fun getExecutorOffers(): Single<List<ExecutorOffer>> {
        return apiHelper.getExecutorOffers()
    }

    fun getWorkerSlot(id: Int): Single<WorkerSlot> {
        return apiHelper.getWorkerSlot(id)
    }

    fun updateWorkerSlot(token: String, workerSlot: WorkerSlot): Single<String> {
        return apiHelper.updateWorkerSlot(token, workerSlot)
    }

    fun inviteProfile(username: String, slotId: Int, token: String): Single<String> {
        return apiHelper.inviteProfile(username, slotId, token)
    }

    fun declineProfile(username: String, slotId: Int, token: String): Single<String> {
        return apiHelper.declineProfile(username, slotId, token)
    }

    fun deleteWorkerSlot(id: Int, token: String): Single<String> {
        return apiHelper.deleteWorkerSlot(id, token)
    }

    fun getSlotApplies(id: Int, token: String): Single<List<Profile>> {
        return apiHelper.getSlotApplies(id, token)
    }

    fun applyToSlot(id: Int, token: String): Single<String> {
        return apiHelper.applyToSlot(id, token)
    }

    fun getAppliedSlots(token: String): Single<List<WorkerSlot>> {
        return apiHelper.getAppliedSlots(token)
    }

    fun acceptInvite(id: Int, token: String): Single<String> {
        return apiHelper.acceptInvite(id, token)
    }

    fun declineInvite(id: Int, token: String): Single<String> {
        return apiHelper.declineInvite(id, token)
    }

    fun getCurrentProjects(token: String): Single<List<ProjectModel>> {
        return apiHelper.getCurrentProjects(token)
    }

    fun leaveWorkerSlot(id: Int, token: String): Single<String> {
        return apiHelper.leaveWorkerSlot(id, token)
    }

    fun getRequestedSlots(token: String): Single<List<WorkerSlot>> {
        return apiHelper.getRequestedSlots(token)
    }

    fun retractRequest(id: Int, token: String): Single<String> {
        return apiHelper.retractRequest(id, token)
    }
}
