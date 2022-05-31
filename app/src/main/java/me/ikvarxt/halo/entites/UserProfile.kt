package me.ikvarxt.halo.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int,
    val nickname: String,
    val username: String,
    val description: String,
    val email: String,
    val avatar: String,
    @ColumnInfo(name = "create_time")
    val createTime: Calendar,
    @ColumnInfo(name = "update_time")
    val updateTime: Calendar,
    @ColumnInfo(name = "mfa_type")
    val mfaType: String,
) {
    val avatarUrl: String
        get() = "https:$avatar"
}
