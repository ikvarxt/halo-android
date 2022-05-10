package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.entites.PostStatus
import me.ikvarxt.halo.entites.network.CreatePostBody
import me.ikvarxt.halo.entites.network.PagesResponse
import me.ikvarxt.halo.network.infra.NetworkResult
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
    ): NetworkResult<PagesResponse<PostItem>>

    // get a post details with slug path
    @GET("posts/slug")
    fun getPostDetailsWithSlug(
        @Query("slug") slug: String,
        @Query("formatEnabled") formatEnabled: Boolean,
    ): NetworkResult<PostDetails>

    // get a post details with post id
    @GET("posts/{postId}")
    suspend fun getPostDetailsWithId(
        @Path("postId") postId: Int,
    ): NetworkResult<PostDetails>

    // create a post
    @POST("posts")
    suspend fun createPost(@Body post: CreatePostBody): NetworkResult<PostItem>

    // update status of a post
    @POST("posts/{postId}/status/{status}")
    suspend fun updateStatusOfPost(
        @Path("postId") postId: Int,
        @Path("status") status: PostStatus
    ): PostItem

    // delete a post permanently
    @DELETE("posts/{postId}")
    suspend fun deletePostPermanently(@Path("postId") postId: Int): NetworkResult<Unit>

    // delete posts in batch
    @DELETE("posts")
    suspend fun deletePostsPermanentlyInBatch(
        @Body postIds: IntArray
    ): List<PostItem>
}