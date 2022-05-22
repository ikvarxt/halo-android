package me.ikvarxt.halo.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.entites.network.CreatePostBody
import me.ikvarxt.halo.entites.network.UpdatePostBody
import me.ikvarxt.halo.network.PostApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(
    private val service: PostApiService,
    private val database: HaloDatabase,
) {
    val dao = database.postItemDao()

    @OptIn(ExperimentalPagingApi::class)
    fun listPosts(pageSize: Int = 10) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PostRemoteMediator(
            apiService = service,
            database = database,
            size = pageSize
        )
    ) {
        dao.pagingSource()
    }.flow

    fun createPost(
        createPost: CreatePostBody
    ): Flow<NetworkResult<PostItem>> = flow {
        val result = service.createPost(createPost)
        emit(result)
    }

    suspend fun updatePost(postId: Int, updatePostBody: UpdatePostBody): PostItem? {
        val result = service.updatePost(postId, updatePostBody)
        return (result as? NetworkResult.Success)?.data
    }

    suspend fun deletePostPermanently(postId: Int): Boolean {
        val result = service.deletePostPermanently(postId)
        return if (result is NetworkResult.Success) {
            database.withTransaction {
                dao.deleteById(postId)
            }
            true
        } else false
    }

    suspend fun getPostDetailsWithPostId(postId: Int) = service.getPostDetailsWithId(postId)
}