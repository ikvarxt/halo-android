package me.ikvarxt.halo.entites

import androidx.room.Ignore
import java.util.*

data class PostComment(
    val id: Int,
    val author: String,
    val authorUrl: String,
    private val avatar: String,
    val content: String,
    val createTime: Calendar,
    val email: String,
    val parentId: Int,
    val post: PostItem,
    // TODO: need change to CommentStatus enum value
    val status: String,
) {
    val avatarUrl: String
        get() = "https:$avatar"

    @Ignore
    var isHighlight: Boolean = false
}
