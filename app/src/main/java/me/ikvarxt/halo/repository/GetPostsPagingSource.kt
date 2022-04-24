package me.ikvarxt.halo.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.PostApiService
import me.ikvarxt.halo.network.infra.NetworkResult

class GetPostsPagingSource(
    private val service: PostApiService,
    private val pageSize: Int
) : PagingSource<Int, PostItem>() {

    override fun getRefreshKey(state: PagingState<Int, PostItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostItem> {
        return try {
            val nextPageIndex = params.key ?: 0
            when (val result = service.listPosts(size = pageSize, page = nextPageIndex)) {
                is NetworkResult.Success -> {
                    val response = result.data
                    LoadResult.Page(
                        data = response.content!!,
                        prevKey = null, // if (response.hasPrevious) response.page - 1 else null,
                        nextKey = if (response.hasNext) response.page + 1 else null
                    )
                }
                is NetworkResult.Failure -> throw Exception(result.msg)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}