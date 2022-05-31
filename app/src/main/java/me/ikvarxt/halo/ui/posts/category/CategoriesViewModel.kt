package me.ikvarxt.halo.ui.posts.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.PostCategory
import me.ikvarxt.halo.repository.CategoriesRepository
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CategoriesRepository
) : ViewModel() {

    val categories = repository.categories

    init {
        viewModelScope.launch {
            repository.listAllCategories()
        }
    }

    fun updateCategory(
        category: PostCategory,
        name: String,
        slug: String
    ) {
        viewModelScope.launch {
            repository.updateCategory(
                id = category.id,
                name = name,
                slug = slug
            )
        }
    }

    fun delete(category: PostCategory) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }
}