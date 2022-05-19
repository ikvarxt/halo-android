package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.PostTag
import me.ikvarxt.halo.entites.network.TagRequestBody
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.*

interface PostTagsAndCategoriesApiService {

    @GET("tags")
    suspend fun getAllTags(
        @Query("more") more: Boolean
    ): NetworkResult<List<PostTag>>

    @POST("tags")
    suspend fun createTag(
        @Body body: TagRequestBody
    ): NetworkResult<PostTag>

    @PUT("tags/{id}")
    suspend fun updateTag(
        @Path("id") tagId: Int,
        @Body body: TagRequestBody,
    ): NetworkResult<PostTag>

    @DELETE("tags/{id}")
    suspend fun deleteTag(
        @Path("id") tagId: Int
    ): NetworkResult<PostTag>
}