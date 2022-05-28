package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.PostCategory
import me.ikvarxt.halo.entites.PostTag
import me.ikvarxt.halo.entites.network.CategoryRequestBody
import me.ikvarxt.halo.entites.network.TagRequestBody
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.*

interface PostTagsAndCategoriesApiService {

    //-- tags
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

    //-- categories
    @GET("categories")
    suspend fun getAllCategories(
        @Query("more") more: Boolean
    ): NetworkResult<List<PostCategory>>

    @GET("categories/{id}")
    suspend fun getCategory(
        @Path("categoryId") id: Int
    ): NetworkResult<PostCategory>

    @POST("categories")
    suspend fun createCategory(
        @Body body: CategoryRequestBody
    ): NetworkResult<PostCategory>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("categoryId") id: Int,
        @Body body: CategoryRequestBody
    ): NetworkResult<PostCategory>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("categoryId") id: Int
    ): NetworkResult<PostCategory>
}