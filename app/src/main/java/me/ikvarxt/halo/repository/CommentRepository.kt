package me.ikvarxt.halo.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.entites.network.CreatePostComment
import me.ikvarxt.halo.entites.network.PagesResponse
import me.ikvarxt.halo.network.CommentApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val service: CommentApiService
) {

    fun pagesPostComments(pageSize: Int = 20) = Pager(
        PagingConfig(pageSize)
    ) {
        PagePostCommentsPagingSource(service, pageSize)
    }.flow

    suspend fun createComment(
        parentComment: PostComment,
        info: UserProfile,
        content: String
    ): String? {
        val postId = parentComment.post?.id ?: return "Error: Post id can NOT be null"
        val requestBody = CreatePostComment(
            author = info.nickname,
            content = content,
            postId = postId,
            parentId = parentComment.id,
            email = info.email,
            null
        )
        return when (val result = service.createPostComment(requestBody)) {
            is NetworkResult.Success -> "Reply Success"
            is NetworkResult.Failure -> result.msg
        }
    }

    suspend fun deletePostComment(comment: PostComment) {
        service.deletePostCommentsRecursively(comment.id)
    }

    fun getCommentsOfPostWithListView(postId: Int): Flow<List<PostComment>> {
        return flow {
            var result = service.getCommentOfPostWithListView(postId, 0)
            val resList = mutableListOf<PostComment>()
            var pages: PagesResponse<PostComment>? = null

            // TODO: currently workaround of pages api call
            do {
                when (result) {
                    is NetworkResult.Success -> {
                        pages = result.data
                        if (pages.hasContent) {
                            resList.addAll(pages.content!!)
                            if (pages.hasNext) {
                                result =
                                    service.getCommentOfPostWithListView(postId, pages.page + 1)
                            }
                        }
                    }
                    is NetworkResult.Failure -> {
                        pages = null
                        Log.d("commentrepo", "getCommentsOfPostWithListView: error")
                    }
                }
            } while (pages?.hasNext == true)

            emit(resList)
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