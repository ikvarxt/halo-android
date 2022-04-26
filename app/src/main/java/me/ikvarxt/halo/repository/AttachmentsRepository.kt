package me.ikvarxt.halo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.ikvarxt.halo.entites.Attachment
import me.ikvarxt.halo.network.AttachmentApiService
import me.ikvarxt.halo.network.DEFAULT_IMAGE_PAGE_SIZE
import me.ikvarxt.halo.network.infra.NetworkResult
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttachmentsRepository @Inject constructor(
    private val apiService: AttachmentApiService
) {

    fun getAttachments(pageSize: Int = DEFAULT_IMAGE_PAGE_SIZE) = Pager(
        PagingConfig(pageSize)
    ) {
        PageAttachmentsPagingSource(apiService, pageSize)
    }.flow

    suspend fun uploadAttachment(body: MultipartBody.Part) = apiService.uploadAttachment(body)

    suspend fun deleteAttachmentPermanently(id: Int) = apiService.permanentlyDeleteAttachment(id)
}

class PageAttachmentsPagingSource(
    private val service: AttachmentApiService,
    private val pageSize: Int
) : PagedPagingSource<Attachment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Attachment> {
        return try {
            val nextPageIndex = params.key ?: 0
            when (val result = service.pageAttachments(page = nextPageIndex, pageSize = pageSize)) {
                is NetworkResult.Success -> {
                    val response = result.data
                    val page = response.page
                    LoadResult.Page(
                        data = response.content!!,
                        prevKey = null,
                        nextKey = if (response.hasNext) page + 1 else null
                    )
                }
                is NetworkResult.Failure -> LoadResult.Error(Exception(result.msg))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}