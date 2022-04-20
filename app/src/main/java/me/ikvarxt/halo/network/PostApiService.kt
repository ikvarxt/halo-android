package me.ikvarxt.halo.network

import androidx.lifecycle.LiveData
import me.ikvarxt.halo.entites.*
import me.ikvarxt.halo.network.infra.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
}