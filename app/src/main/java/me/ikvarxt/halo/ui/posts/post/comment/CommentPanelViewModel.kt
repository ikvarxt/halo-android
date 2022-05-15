package me.ikvarxt.halo.ui.posts.post.comment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.repository.CommentRepository
import javax.inject.Inject

@HiltViewModel
class CommentPanelViewModel @Inject constructor(
    private val repository: CommentRepository
) : ViewModel() {

    fun getCommentList(postId: Int, highlightId: Int): Flow<List<PostComment>> {
        return repository.getCommentsOfPostWithListView(postId)
            .map { commentList ->
                val hashMap = mutableMapOf<PostComment, MutableList<PostComment>>()

                commentList.forEach { comment ->
                    if (comment.id == highlightId) {
                        comment.isHighlight = true
                    }

                    if (comment.parentId != 0) {
                        val parent = commentList.find {
                            it.id == comment.parentId
                        } ?: return@forEach

                        var list = hashMap[parent]
                        if (list == null) {
                            list = mutableListOf()
                        }
                        list.add(comment)
                        hashMap[parent] = list
                    } else if (comment.parentId == 0 && !hashMap.containsKey(comment)) {
                        hashMap[comment] = mutableListOf()
                    }
                }

                val resList = mutableListOf<PostComment>()
                hashMap.forEach { (parent, childList) ->
                    resList.add(parent)
                    resList.addAll(childList)
                }

                resList
            }
    }

}