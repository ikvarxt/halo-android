package me.ikvarxt.halo.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.infra.Resource
import me.ikvarxt.halo.repository.Repository
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val postsList: LiveData<Resource<List<PostItem>>> = repository.getPostsList()

    val navigatePostId = MutableLiveData(0)

    val postDetails = MutableLiveData<PostDetails>()

    fun getPostDetailsWithId(postId: Long) {
        postDetails.value = repository.getPostDetails(postId).value?.data!!
    }
}