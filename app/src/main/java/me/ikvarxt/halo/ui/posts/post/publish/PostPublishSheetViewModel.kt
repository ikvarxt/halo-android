package me.ikvarxt.halo.ui.posts.post.publish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.entites.PostStatus
import me.ikvarxt.halo.entites.network.CreatePostBody
import me.ikvarxt.halo.entites.network.UpdatePostBody
import me.ikvarxt.halo.extentions.HanziToPinyin
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.repository.PostsRepository
import me.ikvarxt.halo.repository.TagsRepository
import javax.inject.Inject

@HiltViewModel
class PostPublishSheetViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val tagsRepository: TagsRepository,
    // private val categoriesRepository:
) : ViewModel(

) {
    val tags = tagsRepository.getAllTags()

    fun createCategory() {

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

    fun generateSlug(title: String, separator: String = "-"): String {
        val hanziToPinyin = HanziToPinyin.getInstance()
        val tokens = hanziToPinyin.get(title)
        return tokens.joinToString("-") { it.target.lowercase() }
    }

    fun publishPost(
        title: String,
        slug: String,
        content: String,
        summary: String? = null,
        createTime: String? = null,
        isDraft: Boolean
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
                createTime = createTime
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
        post: PostDetails,
        title: String = post.title,
        content: String? = post.originalContent,
        slug: String = post.slug,
//        tagsIds:List<Int> = post.
    ) {
        viewModelScope.launch {
            val body = UpdatePostBody(
                title = title,
                content = content,
                slug = slug
            )
            postsRepository.updatePost(post.id, body)
        }
    }
}