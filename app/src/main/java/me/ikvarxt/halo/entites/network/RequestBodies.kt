package me.ikvarxt.halo.entites.network

data class LoginRequestBody(
    val username: String,
    val password: String,
    val authcode: String? = null
)

data class NeedMFACode(
    val needMFACode: Boolean
)