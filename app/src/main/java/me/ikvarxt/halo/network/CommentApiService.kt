package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.entites.network.CreatePostComment
import me.ikvarxt.halo.entites.network.PagesResponse
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.*

interface CommentApiService {

    @GET("posts/comments")
    suspend fun listAllPostComments(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): NetworkResult<PagesResponse<PostComment>>

    @POST("posts/comments")
    suspend fun createPostComment(
        @Body body: CreatePostComment
    ): NetworkResult<PostComment>

    @DELETE("posts/comments/{commentId}")
    suspend fun deletePostCommentsRecursively(
        @Path("commentId") commentId: Int
    ): NetworkResult<PostComment>

    @GET("posts/comments/{postId}/list_view")
    suspend fun getCommentOfPostWithListView(
        @Path("postId") postId: Int,
        @Query("page") page: Int,
    ): NetworkResult<PagesResponse<PostComment>>
}