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

    var post: PostDetails? = null
        private set

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

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

    fun editing(uiState: UiState.EditingUiState) {
        _uiState.value = uiState
    }

    val uiStateTitle: String get() = _uiState.value?.title ?: ""
    val uiStateContent: String get() = _uiState.value?.content ?: ""

    private fun reloadPost() {
        viewModelScope.launch {
            _loading.emit(true)

            when (val result = repository.getPostDetailsWithPostId(postId)) {
                is NetworkResult.Success -> {
                    val postDetails = result.data.also {
                        post = it
                    }

                    _uiState.value = UiState.LoadPostUiState(
                        postDetails.title,
                        postDetails.originalContent ?: ""
                    )
                }
                is NetworkResult.Failure -> {
                    val msg = result.msg ?: "Some error occurred"
                    _error.value = msg
                }
            }

            _loading.emit(false)
        }
    }
}

sealed class UiState(val title: String, val content: String) {

    class LoadPostUiState(title: String, content: String) : UiState(title, content)

    class EditingUiState(title: String, content: String) : UiState(title, content)
}