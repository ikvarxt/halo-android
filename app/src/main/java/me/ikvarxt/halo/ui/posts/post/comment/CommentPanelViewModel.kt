package me.ikvarxt.halo.ui.posts.post.comment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ikvarxt.halo.repository.CommentRepository
import javax.inject.Inject

@HiltViewModel
class CommentPanelViewModel @Inject constructor(
    private val repository: CommentRepository
) : ViewModel() {

    fun getCommentList(postId: Int) = repository.getCommentsOfPostWithListView(postId)
}