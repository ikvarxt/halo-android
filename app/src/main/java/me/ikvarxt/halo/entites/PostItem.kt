package me.ikvarxt.halo.entites

import com.google.gson.annotations.SerializedName

data class ListPostResponse(
    val status: Int,
    val message: String,
    val data: ListPostResponseContent
)

data class ListPostResponseContent(
    @field:SerializedName("content")
    val content: List<PostItem>,
    val total: Int,
)

data class PostItem(
    val id: Int,
    val slug: String,
    val title: String,
    val summary: String,
    val updateTime: String,
    val fullPath: String,
)
