package me.ikvarxt.halo.repository

import androidx.room.withTransaction
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.PostTag
import me.ikvarxt.halo.entites.network.TagRequestBody
import me.ikvarxt.halo.extentions.showNetworkErrorToast
import me.ikvarxt.halo.network.PostTagsAndCategoriesApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagsRepository @Inject constructor(
    private val service: PostTagsAndCategoriesApiService,
    private val db: HaloDatabase
) {
    private val dao = db.postTagsDao()

    val tags = dao.getAllTagsFlow()

    suspend fun getAllTags() {
//        if (refresh) {
//            dao.clearTags()
//        }
        when (val result = service.getAllTags(true)) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.insertTags(result.data)
                }
            }
            is NetworkResult.Failure -> showNetworkErrorToast(result.msg)
        }
    }

    suspend fun updateTag(tag: PostTag): PostTag? {
        try {
            dao.getTag(tag.id)
        } catch (e: Exception) {
            return null
        }

        val body = generateBody(tag)
        when (val result = service.updateTag(tag.id, body)) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.updateTag(result.data)
                }
            }
            is NetworkResult.Failure -> showNetworkErrorToast(result.msg)
        }
        return dao.getTag(tag.id)
    }

    suspend fun createTag(
        name: String,
        slug: String? = null,
        color: String? = null,
        thumbnail: String? = null
    ): PostTag? {
        val body = TagRequestBody(name, slug, color, thumbnail)

        var tag: PostTag? = null

        when (val result = service.createTag(body)) {
            is NetworkResult.Success -> {
                val networkTag = result.data
                db.withTransaction {
                    dao.insertTag(networkTag)
                    tag = dao.getTag(networkTag.id)
                }
            }
            is NetworkResult.Failure -> {}
        }
        return tag
    }

    private fun generateBody(
        tag: PostTag,
        name: String = tag.name,
        slug: String = tag.slug,
        color: String = tag.color,
        thumbnail: String = tag.thumbnail
    ) = TagRequestBody(
        name, slug, color, thumbnail
    )
}