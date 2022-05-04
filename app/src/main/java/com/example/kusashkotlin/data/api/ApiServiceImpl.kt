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

class ApiServiceImpl : ApiService {
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

    override fun sendBelbin(belbinModel: BelbinModel, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/process-belbin/")
            .addJSONObjectBody(JSONObject(Gson().toJson(belbinModel)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)

    }

    override fun sendMBTI(mbtiModel: MBTIModel, token: String): Single<String> {
        return Rx2AndroidNetworking.post("${url}/api/v1/process-mbti/")
            .addJSONObjectBody(JSONObject(Gson().toJson(mbtiModel)))
            .addHeaders("Authorization", "Token $token")
            .build()
            .getObjectSingle(String::class.java)
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
        return Rx2AndroidNetworking.get("${url}/api/v1/get-project/${title}")
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
}
