package me.ikvarxt.halo.ui.posts.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.repository.PostsRepository
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostsRepository,
) : ViewModel() {

    private var postId = 0

    var isWritingMode = false
        private set

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _postLiveData = MutableLiveData<PostDetails>()
    val postLiveData: LiveData<PostDetails> = _postLiveData

    val post: PostDetails?
        get() = postLiveData.value

    val title: String?
        get() = post?.title

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    fun setupArgs(args: PostFragmentArgs) {
        postId = args.postId
        isWritingMode = args.isWriting

        if (!isWritingMode) {
            reloadPost()
        }
    }

    private fun reloadPost() {
        viewModelScope.launch {
            _loading.emit(true)

            when (val result = repository.getPostDetailsWithPostId(postId)) {
                is NetworkResult.Success -> {
                    val post = result.data
                    _postLiveData.value = post
                }
                is NetworkResult.Failure -> {
                    val msg = result.msg ?: "Some error occurred"
                    _error.value = msg
                }
            }

            _loading.emit(false)
        }
    }

    fun publishPost(titleText: String, content: String) {
        viewModelScope.launch {
            val title = titleText.trim()
            repository.createPost(title, content).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val post = it.data
                        _msg.value = "Post: [${post.title}] successfully published"
                    }
                    is NetworkResult.Failure -> {
                        _msg.value = it.msg ?: "Publish failed"
                    }
                }
            }
        }
    }
}