package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.network.LoginRequestBody
import me.ikvarxt.halo.entites.network.LoginToken
import me.ikvarxt.halo.entites.network.NeedMFACode
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * connect with halo.blog/api/admin/
 */
interface AdminApiService {

    @POST("login")
    fun login(
        @Body body: LoginRequestBody
    ): Call<LoginToken>

    @POST("login/precheck")
    fun loginPrecheck(
        @Body body: LoginRequestBody
    ): Call<NeedMFACode>

    @POST("refresh/{refreshToken}")
    fun refreshToken(
        @Path("refreshToken") refreshToken: String,
    ): Call<LoginToken>

    @POST("logout")
    fun logout(): Call<Unit>
}