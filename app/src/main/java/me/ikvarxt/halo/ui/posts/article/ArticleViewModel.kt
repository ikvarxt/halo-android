package me.ikvarxt.halo.ui.posts.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.network.infra.Resource
import me.ikvarxt.halo.repository.Repository
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val postId = MutableLiveData<Long>()

    val postDetails: LiveData<Resource<PostDetails>> = Transformations.switchMap(postId) {
        repository.getPostDetails(it)
    }

    fun setPostId(id: Long) {
        postId.value = id
    }
}