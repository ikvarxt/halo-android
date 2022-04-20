package me.ikvarxt.halo.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.PostApiService

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
            val response = service.listPosts(size = pageSize, page = nextPageIndex)
            LoadResult.Page(
                data = response.content!!,
                prevKey = null, // if (response.hasPrevious) response.page - 1 else null,
                nextKey = if (response.hasNext) response.page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}