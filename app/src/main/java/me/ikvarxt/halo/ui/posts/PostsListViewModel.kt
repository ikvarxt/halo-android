package me.ikvarxt.halo.ui.posts

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

//    private val _refreshLiveData = MutableLiveData(0)

//    val postsList: LiveData<Resource<List<PostItem>>> =
//        Transformations.switchMap(_refreshLiveData) {
//            repository.getPostsList()
//        }

//    fun refresh() {
//        _refreshLiveData.value = 0
//    }

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

    fun deletePostPermanently(postId: Int) {
        viewModelScope.launch {
            _operationInProgress.emit(true)
            // TODO: need deal with the operation's status of success and failure
            val response = postsRepository.deletePostPermanently(postId)
            _operationInProgress.emit(false)
            _refreshAdapter.emit(Any())
        }
    }
}