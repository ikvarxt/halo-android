package me.ikvarxt.halo.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.entites.network.CreatePostBody
import me.ikvarxt.halo.network.PostApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(
    private val service: PostApiService,
    private val database: HaloDatabase,
) {
    @OptIn(ExperimentalPagingApi::class)
    fun listPosts(pageSize: Int = 10) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PostRemoteMediator(
            apiService = service,
            database = database,
            size = pageSize
        )
    ) {
        database.postItemDao().pagingSource()
    }.flow

    fun createPost(
        title: String,
        content: String,
    ): Flow<NetworkResult<PostItem>> = flow {
        val body = CreatePostBody(title, content)
        val result = service.createPost(body)
        emit(result)
    }

    suspend fun deletePostPermanently(postId: Int) = service.deletePostPermanently(postId)

    suspend fun getPostDetailsWithPostId(postId: Int) = service.getPostDetailsWithId(postId)
}