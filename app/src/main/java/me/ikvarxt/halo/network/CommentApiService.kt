package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.entites.network.CreatePostComment
import me.ikvarxt.halo.entites.network.PagesResponse
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

}