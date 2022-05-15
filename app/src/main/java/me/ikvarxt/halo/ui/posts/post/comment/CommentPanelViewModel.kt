package me.ikvarxt.halo.ui.posts.post.comment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.repository.CommentRepository
import javax.inject.Inject

@HiltViewModel
class CommentPanelViewModel @Inject constructor(
    private val repository: CommentRepository
) : ViewModel() {

    fun getCommentList(postId: Int): Flow<List<PostComment>> =
        repository.getCommentsOfPostWithListView(postId)
}