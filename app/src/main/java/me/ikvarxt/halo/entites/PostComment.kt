package me.ikvarxt.halo.entites

import java.util.*

data class PostComment(
    val id: Int,
    val author: String,
    val authorUrl: String,
    val avatar: String,
    val content: String,
    val createTime: Calendar,
    val email: String,
    val parentId: Int,
    val post: PostItem,
    // TODO: need change to CommentStatus enum value
    val status: String,
)
