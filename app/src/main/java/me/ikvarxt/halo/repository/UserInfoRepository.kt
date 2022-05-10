package me.ikvarxt.halo.repository

import me.ikvarxt.halo.network.UserApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepository @Inject constructor(
    private val service: UserApiService
) {

    suspend fun getUserProfile() = service.getUserProfiles()
}