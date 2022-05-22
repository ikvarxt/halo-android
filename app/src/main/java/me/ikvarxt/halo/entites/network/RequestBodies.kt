package me.ikvarxt.halo.entites.network

import com.google.gson.annotations.SerializedName
import me.ikvarxt.halo.entites.PostEditorType
import me.ikvarxt.halo.entites.PostStatus

data class LoginRequestBody(
    val username: String,
    val password: String,
    val authcode: String? = null
)

data class NeedMFACode(
    val needMFACode: Boolean
)

data class CreatePostBody(
    val title: String,
    @SerializedName("originalContent")
    val content: String? = null,
    val createTime: String? = null,
    val updateTime: String? = null,
    val editTime: String? = null,
    val summary: String? = null,
    val status: PostStatus = PostStatus.DRAFT,
    val thumbnail: String? = null,
    val slug: String? = null,
    val editorType: PostEditorType = PostEditorType.MARKDOWN,
    val disallowComment: Boolean = false,
    val categoryIds: List<Int>? = null,
    val tagIds: List<Int>? = null,
    val password: String? = null,
)

data class CreatePostComment(
    val author: String,
    val content: String,
    val postId: Int,
    val parentId: Int? = null,
    val email: String? = null,
    val authorUrl: String? = null,
    val allowNotification: Boolean? = null
)

data class TagRequestBody(
    val name: String,
    val slug: String? = null,
    val color: String? = null,
    val thumbnail: String? = null,
)

data class UpdatePostBody(
    val title: String,
    @SerializedName("originalContent")
    val content: String?,
    val slug: String,
    val summary: String? = null,
//    val status: PostStatus,
//    val tagIds: List<Int>,
    val thumbnail: String? = null,
    val disallowComment: Boolean = false,
    val password: String? = null,
)