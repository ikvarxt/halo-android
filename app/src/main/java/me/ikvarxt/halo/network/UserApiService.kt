package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.GET

interface UserApiService {

    @GET("users/profiles")
    suspend fun getUserProfiles(): NetworkResult<UserProfile>
}