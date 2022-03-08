package me.ikvarxt.halo.network

import androidx.lifecycle.LiveData
import me.ikvarxt.halo.entites.HaloResponse
import me.ikvarxt.halo.entites.ListPostResponse
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.network.infra.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentApiService {

    // list posts
    @GET("${CONTENT_BASE}posts")
    fun listPosts(@Query("api_access_key") key: String = "3PNtb5tDV37UpCCTNbGzUNqwZ"):
            LiveData<ApiResponse<HaloResponse<ListPostResponse>>>

    // get a post details with slug path
    @GET("${CONTENT_BASE}posts/slug")
    fun getPostDetailsWithSlug(
        @Query("slug") slug: String,
        @Query("formatEnabled") formatEnabled: Boolean,
        @Query("api_accesss_key") key: String = Constants.accessKey,
    ): LiveData<ApiResponse<HaloResponse<PostDetails>>>

    // get a post details with post id
    @GET("${CONTENT_BASE}posts/{postId}")
    fun getPostDetailsWithId(
        @Path("postId") postId: Long,
        @Query("api_access_key") key: String = Constants.accessKey
    ): LiveData<ApiResponse<HaloResponse<PostDetails>>>

    companion object {
        const val CONTENT_BASE = "content/"
    }
}