package me.ikvarxt.halo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.network.CommentApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentApiService: CommentApiService
) {

    fun pagesPostComments(pageSize: Int = 20) = Pager(
        PagingConfig(pageSize)
    ) {
        PagePostCommentsPagingSource(commentApiService, pageSize)
    }.flow
}

class PagePostCommentsPagingSource(
    private val service: CommentApiService,
    private val pageSize: Int
) : PagedPagingSource<PostComment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostComment> {
        return try {
            val nextPageIndex = params.key ?: 0
            when (val result = service.listAllPostComments(page = nextPageIndex, size = pageSize)) {
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