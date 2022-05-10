package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.Attachment
import me.ikvarxt.halo.entites.network.PagesResponse
import me.ikvarxt.halo.network.infra.NetworkResult
import okhttp3.MultipartBody
import retrofit2.http.*

val DEFAULT_IMAGE_PAGE_SIZE = 5

interface AttachmentApiService {

    @GET("attachments")
    suspend fun pageAttachments(
        @Query("page") page: Int,
        @Query("size") pageSize: Int = DEFAULT_IMAGE_PAGE_SIZE
    ): NetworkResult<PagesResponse<Attachment>>

    @Multipart
    @POST("attachments/upload")
    suspend fun uploadAttachment(@Part file: MultipartBody.Part): NetworkResult<Attachment>

    @DELETE("attachments/{id}")
    suspend fun permanentlyDeleteAttachment(@Path("id") id: Int): NetworkResult<Attachment>
}