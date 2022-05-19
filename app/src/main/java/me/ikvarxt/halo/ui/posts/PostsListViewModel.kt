package me.ikvarxt.halo.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.ikvarxt.halo.repository.PostsRepository
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    val pagingPostsData = postsRepository.listPosts()

    /**
     * indicates that there is a background operation currently in progress or not
     */
    private val _operationInProgress = MutableStateFlow(false)
    val operationInProgress: StateFlow<Boolean> = _operationInProgress

    /**
     * trigger paging adapter refresh flow.
     * Learned that emit a StateFlow of Unit can never trigger collect
     */
    private val _refreshAdapter = MutableStateFlow(Any())
    val refreshAdapter: StateFlow<Any> = _refreshAdapter

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    fun deletePostPermanently(postId: Int) {
        viewModelScope.launch {
            _operationInProgress.emit(true)
            val isSuccess = postsRepository.deletePostPermanently(postId)
            if (!isSuccess) _msg.value = "Error occurred, delete failed"
            _operationInProgress.emit(false)
        }
    }
}