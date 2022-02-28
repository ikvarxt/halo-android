package me.ikvarxt.halo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.infra.Resource
import me.ikvarxt.halo.repository.Repository
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val postsList: LiveData<Resource<List<PostItem>>> = repository.getPostsList()

}