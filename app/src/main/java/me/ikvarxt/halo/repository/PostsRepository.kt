package me.ikvarxt.halo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.entites.network.CreatePostBody
import me.ikvarxt.halo.network.PostApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(
    private val service: PostApiService,
) {
    fun listPosts(pageSize: Int = 10) = Pager(
        PagingConfig(pageSize)
    ) {
        GetPostsPagingSource(service, pageSize)
    }.flow

    fun createPost(
        title: String,
        content: String,
    ): Flow<Result<PostItem>> {
        return flow {
            try {
                val body = CreatePostBody(title, content)
                val response = service.createPost(body)
                emit(Result.success(response))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }
}