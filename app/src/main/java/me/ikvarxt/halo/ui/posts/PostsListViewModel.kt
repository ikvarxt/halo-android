package me.ikvarxt.halo.ui.posts

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.infra.Resource
import me.ikvarxt.halo.repository.GetPostsPagingSource
import me.ikvarxt.halo.repository.PostsRepository
import me.ikvarxt.halo.repository.Repository
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val repository: Repository,
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _refreshLiveData = MutableLiveData(0)

//    val postsList: LiveData<Resource<List<PostItem>>> =
//        Transformations.switchMap(_refreshLiveData) {
//            repository.getPostsList()
//        }

    fun refresh() {
        _refreshLiveData.value = 0
    }

    val pagingPostsData = postsRepository.listPosts()
}