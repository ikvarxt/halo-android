package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.network.LoginRequestBody
import me.ikvarxt.halo.entites.network.LoginToken
import me.ikvarxt.halo.entites.network.NeedMFACode
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * connect with halo.blog/api/admin/
 */
interface AdminApiService {

    @POST("login")
    suspend fun login(
        @Body body: LoginRequestBody
    ): NetworkResult<LoginToken>

    @POST("login/precheck")
    suspend fun loginPrecheck(
        @Body body: LoginRequestBody
    ): NetworkResult<NeedMFACode>

    @POST("refresh/{refreshToken}")
    suspend fun refreshToken(
        @Path("refreshToken") refreshToken: String,
    ): NetworkResult<LoginToken>

    @POST("logout")
    suspend fun logout(): NetworkResult<Unit>
}