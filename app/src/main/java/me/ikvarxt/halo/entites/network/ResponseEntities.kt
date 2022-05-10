package me.ikvarxt.halo.entites.network

import com.google.gson.annotations.SerializedName

data class LoginToken(
    @field:SerializedName("access_token") val accessToken: String,
    @field:SerializedName("expired_in") val expiredIn: Long,
    @field:SerializedName("refresh_token") val refreshToken: String,
)

data class PagesResponse<T>(
    val content: List<T>?,
    val hasContent: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val isEmpty: Boolean,
    val isFirst: Boolean,
    val isLast: Boolean,
    val page: Int,
    val pages: Int,
    val rpp: Int,
    val total: Int,
)