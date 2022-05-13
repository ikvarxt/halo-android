package me.ikvarxt.halo.entites

import java.util.*

data class UserProfile(
    val id: Int,
    val nickname: String,
    val username: String,
    val description: String,
    val email: String,
    private val avatar: String,
    val createTime: Calendar,
    val updateTime: Calendar,
    val mfaType: String,
) {
    val avatarUrl: String
        get() = "https:$avatar"
}
