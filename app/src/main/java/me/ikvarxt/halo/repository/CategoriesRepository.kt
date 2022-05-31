package me.ikvarxt.halo.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.PostCategory
import me.ikvarxt.halo.entites.network.CategoryRequestBody
import me.ikvarxt.halo.network.PostTagsAndCategoriesApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRepository @Inject constructor(
    private val service: PostTagsAndCategoriesApiService,
    private val db: HaloDatabase
) {
    private val dao = db.postCategoriesDao()

    fun listAllCategories(): Flow<List<PostCategory>> = flow {
        when (val result = service.getAllCategories(true)) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.clearCategories()
                    dao.insertCategories(result.data)
                }
            }
            is NetworkResult.Failure -> {}
        }
        val categories = dao.getAllCategories()
        emit(categories)
    }

    suspend fun createCategory(
        name: String,
        slug: String,
        parentId: Int? = 0,
        id: Int? = null,
    ): PostCategory? {
        val body = CategoryRequestBody(id, name, slug, parentId)
        when (val result = service.createCategory(body)) {
            is NetworkResult.Success -> {
                val data = result.data
                db.withTransaction {
                    dao.insertCategory(data)
                }
                return dao.getCategory(data.id)
            }
            is NetworkResult.Failure -> {}
        }
        return null
    }

    suspend fun updateCategory(
        id: Int,
        name: String,
        slug: String
    ): PostCategory? {
        val body = CategoryRequestBody(
            name = name,
            slug = slug,
        )
        val result = service.updateCategory(id, body)
        when (result) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.updateCategory(result.data)
                }
                return dao.getCategory(id)
            }
            is NetworkResult.Failure -> {
                return null
            }
        }
    }

    suspend fun deleteCategory(category: PostCategory) {
        service.deleteCategory(category.id)
    }
}