package me.ikvarxt.halo.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.repository.CommentRepository
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: CommentRepository
) : ViewModel() {

    val pastsComments = repository.pagesPostComments()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun replyComment(commentItem: PostComment, info: UserProfile, content: String) {
        viewModelScope.launch {
            val msg = repository.createComment(commentItem, info, content)
            msg?.let { _message.value = it }
        }
    }

}