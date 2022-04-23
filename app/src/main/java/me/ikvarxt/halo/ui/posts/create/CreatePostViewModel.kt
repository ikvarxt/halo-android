package me.ikvarxt.halo.ui.posts.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.repository.PostsRepository
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val repository: PostsRepository
) : ViewModel() {

    var title: String = ""

    private val _result = MutableLiveData<NetworkResult<PostItem>>()
    val result: LiveData<NetworkResult<PostItem>> = _result

    fun publishPost(content: String) {
        viewModelScope.launch {
            val res = repository.createPost(title, content)
            res.collectLatest {
                _result.value = it
            }
        }
    }
}