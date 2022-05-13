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
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _postDetails = MutableLiveData<NetworkResult<PostDetails>>()
    val postDetails: LiveData<NetworkResult<PostDetails>> = _postDetails

    fun setPostId(id: Int) {
        postId = id
        reloadPost()
    }

    private fun reloadPost() {
        viewModelScope.launch {
            _loading.emit(true)
            _postDetails.value = repository.getPostDetailsWithPostId(postId)
            _loading.emit(false)
        }
    }
}