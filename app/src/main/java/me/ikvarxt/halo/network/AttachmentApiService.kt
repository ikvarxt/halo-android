package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.Attachment
import me.ikvarxt.halo.entites.PagesResponse
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

val DEFAULT_IMAGE_PAGE_SIZE = 5

interface AttachmentApiService {

    @GET("attachments")
    suspend fun pageAttachments(
        @Query("page") page: Int,
        @Query("size") pageSize: Int = DEFAULT_IMAGE_PAGE_SIZE
    ): NetworkResult<PagesResponse<Attachment>>
}