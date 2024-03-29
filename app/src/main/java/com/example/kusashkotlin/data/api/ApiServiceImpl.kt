package com.example.kusashkotlin.data.api

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.example.kusashkotlin.App
import com.example.kusashkotlin.R
import com.example.kusashkotlin.data.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import io.reactivex.Single
import org.json.JSONObject
import org.json.JSONStringer


// интерфейс ApiService и его реализацию нужно бить по доменам. Не надо все запихивать в один огромный класс.

// Работу с токенами лучше сделать по другому, сейчас у тебя каждый кто вызывает методы сервиса должен знать token, это лишнее
// Его можно инкапсулировать тут или в ApiHelper - тогда в нем появляется какой-то смысл
// ну и разносить на разные сервисы - один для авторизованной зоны - с токеном, другой для неавторизованной
// Строковые значения, особенно те которые повторяются надо выносить в константы.
// в этом случае как минимум: "Authorization",  "Token". но "username", "password" я бы тоже вынес


class ApiServiceImpl : ApiService {
    // так делать не надо, если хочется конфигурировать - лучше переменный положить в BuildConfig. И не надо будет делать хак с ресурсами
    // Но если даже оставлять ресурсы как есть, то лучше их передавать через конструктор а не через инстанс App
    private val host = App.getAppResources().getString(R.string.host)
    private val protocol = App.getAppResources().getString(R.string.protocol)
    private val port = App.getAppResources().getString(R.string.port)
    private val url = "${protocol}://${host}:${port}"


    override fun getProfile(username: String): Single<Profile> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-profile/${username}/")
            .build()
            .getObjectSingle(Profile::class.java)
    }

    override fun getToken(login: String, password: String): Single<TokenResponse> {
        return Rx2AndroidNetworking.post("${url}/auth/token/login/")
            .addBodyParameter("username", login)
            .addBodyParameter("password", password)
            .build()
            .getObjectSingle(TokenResponse::class.java)
    }

    override fun getUserByToken(token: String): Single<User> {
        return Rx2AndroidNetworking.get("${url}/auth/users/me/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(User::class.java)
    }

    override fun registerUser(
        email: String,
        username: String,
        password: String
    ): Single<RegisterResponse> {
        return Rx2AndroidNetworking.post("${url}/auth/users/")
            .addBodyParameter("email", email)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .build()
            .getObjectSingle(RegisterResponse::class.java)
    }

    override fun editProfile(update: ProfileUpdate, token: String): Single<String> {
        return Rx2AndroidNetworking.patch("${url}/api/v1/edit-profile/")
            .addJSONObjectBody(JSONObject(Gson().toJson(update)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun sendBelbin(belbinModel: BelbinModel, token: String): Single<List<String>> {
        return Rx2AndroidNetworking.post("${url}/api/v1/process-belbin/")
            .addJSONObjectBody(JSONObject(Gson().toJson(belbinModel)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectListSingle(String::class.java)

    }

    override fun sendMBTI(mbtiModel: MBTIModel, token: String): Single<List<String>> {
        return Rx2AndroidNetworking.post("${url}/api/v1/process-mbti/")
            .addJSONObjectBody(JSONObject(Gson().toJson(mbtiModel)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectListSingle(String::class.java)
    }

    override fun updateExecutorOffer(
        executorOfferSetupModel: ExecutorOfferSetupModel,
        token: String
    ): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/update-executor-offer/")
            .addJSONObjectBody(JSONObject(Gson().toJson(executorOfferSetupModel)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun deleteExecutorOffer(token: String): Single<String> {
        return Rx2AndroidNetworking.delete("${url}/api/v1/delete-executor-offer/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun getSpecializations(): Single<List<SpecializationModel>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-specialiazations/")
            .build()
            .getObjectListSingle(SpecializationModel::class.java)
    }

    override fun getBelbinRoles(): Single<List<RoleModel>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-belbin-roles/")
            .build()
            .getObjectListSingle(RoleModel::class.java)
    }

    override fun updateProject(projectModel: ProjectModel, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/update-project/")
            .addHeaders("Authorization", "Token $token")
            .addJSONObjectBody(JSONObject(Gson().toJson(projectModel)))
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun getProject(title: String): Single<ProjectModel> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-project/${title}/")
            .build()
            .getObjectSingle(ProjectModel::class.java)
    }

    override fun deleteProject(token: String): Single<String> {
        return Rx2AndroidNetworking.delete("${url}/api/v1/delete-project/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun getProjects(): Single<List<ProjectModel>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-projects/")
            .build()
            .getObjectListSingle(ProjectModel::class.java)
    }

    override fun getExecutorOffers(): Single<List<ExecutorOffer>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-executor-offers/")
            .build()
            .getObjectListSingle(ExecutorOffer::class.java)
    }

    override fun getWorkerSlot(id: Int): Single<WorkerSlot> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-worker-slot/${id}/")
            .build()
            .getObjectSingle(WorkerSlot::class.java)
    }

    override fun updateWorkerSlot(token: String, workerSlot: WorkerSlot): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/update-worker-slot/")
            .addHeaders("Authorization", "Token $token")
            .addJSONObjectBody(JSONObject(Gson().toJson(workerSlot)))
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun inviteProfile(username: String, slotId: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/invite-profile/${username}/${slotId}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun declineProfile(username: String, slotId: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/decline-apply-slot/${username}/${slotId}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun deleteWorkerSlot(id: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.delete("${url}/api/v1/delete-worker-slot/${id}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun getSlotApplies(id: Int, token: String): Single<List<Profile>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-slot-applies/${id}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectListSingle(Profile::class.java)
    }

    override fun applyToWorkerSlot(id: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/apply-slot/${id}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun getAppliedSlots(token: String): Single<List<WorkerSlot>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-invited-slots/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectListSingle(WorkerSlot::class.java)
    }

    override fun acceptInvite(id: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/accept-invite/${id}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun declineInvite(id: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/decline-invite/${id}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun getCurrentProjects(token: String): Single<List<ProjectModel>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-current-projects/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectListSingle(ProjectModel::class.java)
    }

    override fun leaveWorkerSlot(id: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.get("${url}/api/v1/leave-slot/${id}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

    override fun getRequestedSlots(token: String): Single<List<WorkerSlot>> {
        return Rx2AndroidNetworking.get("${url}/api/v1/get-applied-slots/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectListSingle(WorkerSlot::class.java)
    }

    override fun retractRequest(id: Int, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/retract-apply/${id}/")
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
    }

}
