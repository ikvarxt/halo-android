package me.ikvarxt.halo.ui.posts.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.PostTag
import me.ikvarxt.halo.repository.TagsRepository
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val repository: TagsRepository
) : ViewModel() {

    val tags: Flow<List<PostTag>> = repository.tags

    init {
        viewModelScope.launch {
            repository.getAllTags()
        }
    }

    fun updateTagName(tag: PostTag, name: String) {
        viewModelScope.launch {
            val newTag = tag.copy(name = name)
            repository.updateTag(newTag)
        }
    }

    fun updateTag(tag: PostTag) {
        viewModelScope.launch {
            repository.updateTag(tag)
        }
    }
}