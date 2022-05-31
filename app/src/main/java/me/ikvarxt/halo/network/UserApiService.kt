package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.entites.network.UserProfileRequestBody
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApiService {

    @GET("users/profiles")
    suspend fun getUserProfiles(): NetworkResult<UserProfile>

    @PUT("users/profiles")
    suspend fun updateProfiles(
        @Body body: UserProfileRequestBody
    ): NetworkResult<UserProfile>
}