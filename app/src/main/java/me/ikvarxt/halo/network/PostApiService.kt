package me.ikvarxt.halo.network

import androidx.lifecycle.LiveData
import me.ikvarxt.halo.entites.*
import me.ikvarxt.halo.entites.network.CreatePostBody
import me.ikvarxt.halo.network.infra.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface PostApiService {

    // list posts
    @GET("posts")
    suspend fun listPosts(
        @Query("categoryId") categoryId: Int? = null,
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sorts: Array<String>? = null,
        @Query("status") status: PostStatus? = null,
        @Query("statuses") statuses: Array<String>? = null,
        @Query("more") more: Boolean? = null
    ): PagesResponse<PostItem>

    // get a post details with slug path
    @GET("posts/slug")
    fun getPostDetailsWithSlug(
        @Query("slug") slug: String,
        @Query("formatEnabled") formatEnabled: Boolean,
    ): LiveData<ApiResponse<PostDetails>>

    // get a post details with post id
    @GET("posts/{postId}")
    fun getPostDetailsWithId(
        @Path("postId") postId: Int,
    ): LiveData<ApiResponse<PostDetails>>

    // create a post
    @POST("posts")
    suspend fun createPost(@Body post: CreatePostBody): PostItem

    // update status of a post
    @POST("posts/{postId}/status/{status}")
    suspend fun updateStatusOfPost(
        @Path("postId") postId: Int,
        @Path("status") status: PostStatus
    ): PostItem

    // delete a post permanently
    @DELETE("posts/{postId}")
    suspend fun deletePostPermanently(@Path("postId") postId: Int): Response<Unit>

    // delete posts in batch
    @DELETE("posts")
    suspend fun deletePostsPermanentlyInBatch(
        @Body postIds: IntArray
    ): List<PostItem>
}