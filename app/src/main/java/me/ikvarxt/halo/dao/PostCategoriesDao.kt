package me.ikvarxt.halo.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.ikvarxt.halo.entites.PostCategory

@Dao
interface PostCategoriesDao {

    @Query("SELECT * FROM post_categories ORDER BY create_time ASC")
    suspend fun getAllCategories(): List<PostCategory>

    @Query("SELECT * FROM post_categories ORDER BY create_time ASC")
    fun getAllCategoriesFlow(): Flow<List<PostCategory>>

    @Query("SELECT * FROM post_categories WHERE id LIKE :id")
    suspend fun getCategory(id: Int): PostCategory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(list: List<PostCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: PostCategory)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: PostCategory)

    @Query("DELETE FROM post_categories")
    suspend fun clearCategories()

    @Delete
    suspend fun deleteCategory(category: PostCategory)
}