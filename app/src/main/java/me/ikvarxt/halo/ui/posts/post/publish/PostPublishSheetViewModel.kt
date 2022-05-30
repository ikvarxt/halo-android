package me.ikvarxt.halo.ui.posts.post.publish

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.PostStatus
import me.ikvarxt.halo.entites.network.CreatePostBody
import me.ikvarxt.halo.entites.network.PostDetailsBody
import me.ikvarxt.halo.entites.network.UpdatePostBody
import me.ikvarxt.halo.extentions.toSlug
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.repository.CategoriesRepository
import me.ikvarxt.halo.repository.PostsRepository
import me.ikvarxt.halo.repository.TagsRepository
import javax.inject.Inject

@HiltViewModel
class PostPublishSheetViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val tagsRepository: TagsRepository,
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    val tags = tagsRepository.getAllTags().map { flow ->
        flow.map {
            val color = Color.parseColor(it.color)
            SelectedItem(it.id, it.name, color)
        }
    }

    val categories = categoriesRepository.listAllCategories().map { flow ->
        flow.map { category ->
            SelectedItem(category.id, category.name)
        }
    }

    fun createCategory(
        name: String,
        slug: String = name.toSlug(),
    ) {
        viewModelScope.launch {
            val category = categoriesRepository.createCategory(name = name, slug = slug)
        }
    }

    fun createTag(
        name: String,
        slug: String? = null,
        color: String? = null,
        thumbnail: String? = null
    ) {
        viewModelScope.launch {
            val tag = tagsRepository.createTag(name, slug, color, thumbnail)

        }
    }

    fun publishPost(
        title: String,
        slug: String,
        content: String,
        summary: String?,
        createTime: String? = null,
        isDraft: Boolean,
        categories: List<Int>? = null,
        tags: List<Int>? = null,
    ) {
        viewModelScope.launch {
            val status = if (isDraft) {
                PostStatus.DRAFT
            } else {
                PostStatus.PUBLISHED
            }

            val body = CreatePostBody(
                title = title,
                content = content,
                slug = slug,
                status = status,
                summary = summary,
                createTime = createTime,
                categoryIds = categories,
                tagIds = tags,
            )

            postsRepository.createPost(body).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val post = it.data
//                        _msg.value = "Post: [${post.title}] successfully published"
                    }
                    is NetworkResult.Failure -> {
//                        _msg.value = it.msg ?: "Publish failed"
                    }
                }
            }
        }
    }

    fun updatePost(
        post: PostDetailsBody,
        title: String = post.title,
        content: String? = post.originalContent,
        summary: String? = post.summary,
        slug: String = post.slug,
        isDraft: Boolean,
        tagsIds: List<Int> = post.tags.map { it.id },
        categories: List<Int> = post.categories.map { it.id }
    ) {
        viewModelScope.launch {
            val status = if (isDraft) {
                PostStatus.DRAFT
            } else PostStatus.PUBLISHED

            val body = UpdatePostBody(
                title = title,
                content = content,
                slug = slug,
                status = status,
                summary = summary,
                tagIds = tagsIds,
                categoryIds = categories,
            )

            val item = postsRepository.updatePost(post.id, body)
        }
    }
}