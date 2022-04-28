package me.ikvarxt.halo.ui.comment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ikvarxt.halo.repository.CommentRepository
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    repository: CommentRepository
) : ViewModel() {

    val pastsComments = repository.pagesPostComments()

}