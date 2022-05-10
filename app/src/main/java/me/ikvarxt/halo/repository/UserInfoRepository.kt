package me.ikvarxt.halo.repository

import me.ikvarxt.halo.network.UserApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepository @Inject constructor(
    private val service: UserApiService
) {

    suspend fun getUserProfile() = when (val result = service.getUserProfiles()) {
        is NetworkResult.Success -> result.data
        is NetworkResult.Failure -> null
    }

}