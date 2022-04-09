package me.ikvarxt.halo.network

import androidx.lifecycle.LiveData
import me.ikvarxt.halo.entites.HaloResponse
import me.ikvarxt.halo.entites.ListPostResponse
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.network.infra.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {

    // list posts
    @GET("posts")
    fun listPosts(): LiveData<ApiResponse<HaloResponse<ListPostResponse>>>

    // get a post details with slug path
    @GET("posts/slug")
    fun getPostDetailsWithSlug(
        @Query("slug") slug: String,
        @Query("formatEnabled") formatEnabled: Boolean,
    ): LiveData<ApiResponse<HaloResponse<PostDetails>>>

    // get a post details with post id
    @GET("posts/{postId}")
    fun getPostDetailsWithId(
        @Path("postId") postId: Long,
    ): LiveData<ApiResponse<HaloResponse<PostDetails>>>
}