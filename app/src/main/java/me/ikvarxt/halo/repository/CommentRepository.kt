package me.ikvarxt.halo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.entites.network.CreatePostComment
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

    suspend fun createComment(comment: PostComment, info: UserProfile, content: String): String? {
        val requestBody = CreatePostComment(
            author = info.nickname,
            content = content,
            postId = comment.post.id,
            parentId = comment.parentId,
            email = info.email,
            null
        )
        val result = commentApiService.createPostComment(requestBody)
        return when (result) {
            is NetworkResult.Success -> "Reply Success"
            is NetworkResult.Failure -> result.msg
        }
    }
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