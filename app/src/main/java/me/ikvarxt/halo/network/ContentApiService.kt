package me.ikvarxt.halo.network

import androidx.lifecycle.LiveData
import me.ikvarxt.halo.entites.ListPostResponse
import me.ikvarxt.halo.network.infra.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ContentApiService {

    @GET("${CONTENT_BASE}posts")
    fun listPosts(@Query("api_access_key") key: String = "3PNtb5tDV37UpCCTNbGzUNqwZ"): LiveData<ApiResponse<ListPostResponse>>

    companion object {
        const val CONTENT_BASE = "content/"
    }
}