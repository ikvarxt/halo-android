package me.ikvarxt.halo.entites.network

import com.google.gson.annotations.SerializedName

data class LoginToken(
    @field:SerializedName("access_token") val accessToken: String,
    @field:SerializedName("expired_in") val expiredIn: Long,
    @field:SerializedName("refresh_token") val refreshToken: String,
)