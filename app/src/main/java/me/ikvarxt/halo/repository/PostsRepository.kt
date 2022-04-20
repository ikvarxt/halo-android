package me.ikvarxt.halo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.ikvarxt.halo.network.PostApiService
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val service: PostApiService,
) {
    fun listPosts(pageSize: Int = 10) = Pager(
        PagingConfig(pageSize)
    ) {
        GetPostsPagingSource(service, pageSize)
    }.flow
}