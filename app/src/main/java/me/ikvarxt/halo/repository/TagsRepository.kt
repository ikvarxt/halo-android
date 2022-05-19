package me.ikvarxt.halo.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.PostTag
import me.ikvarxt.halo.entites.network.TagRequestBody
import me.ikvarxt.halo.network.PostTagsAndCategoriesApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagsRepository @Inject constructor(
    private val service: PostTagsAndCategoriesApiService,
    private val db: HaloDatabase
) {
    val dao = db.postTagsDao()

    fun getAllTags(): Flow<List<PostTag>> = flow {
//        if (refresh) {
//            dao.clearTags()
//        }
        when (val result = service.getAllTags(true)) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.insertTags(result.data)
                }
            }
            else -> {}
        }
        val tags = dao.getAllTags()
        emit(tags)
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
            else -> {}
        }
        return dao.getTag(tag.id)
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