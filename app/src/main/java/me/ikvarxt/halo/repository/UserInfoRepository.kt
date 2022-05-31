package me.ikvarxt.halo.repository

import androidx.room.withTransaction
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.network.UserProfileRequestBody
import me.ikvarxt.halo.extentions.showNetworkErrorToast
import me.ikvarxt.halo.network.UserApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepository @Inject constructor(
    private val service: UserApiService,
    private val db: HaloDatabase
) {
    private val dao = db.userProfileDao()

    val userProfile = dao.getUserProfile()

    suspend fun getUserProfile() = when (val result = service.getUserProfiles()) {
        is NetworkResult.Success -> {
            db.withTransaction {
                dao.insertProfile(result.data)
                result.data
            }
        }
        is NetworkResult.Failure -> null
    }

    suspend fun updateProfile(
        username: String, nickname: String = username, email: String,
        avatar: String? = null, description: String? = null, password: String? = null
    ) {
        val body = UserProfileRequestBody(username, nickname, email, avatar, description, password)

        val result = service.updateProfiles(body)
        when (result) {
            is NetworkResult.Success ->
                db.withTransaction {
                    dao.clearProfile()
                    dao.insertProfile(result.data)
                }
            is NetworkResult.Failure -> showNetworkErrorToast(result.msg)
        }
    }

}