package me.ikvarxt.halo.dao

import androidx.room.*
import me.ikvarxt.halo.entites.PostTag

@Dao
interface PostTagsDao {

    @Query("SELECT * FROM post_tags ORDER BY create_time ASC")
    suspend fun getAllTags(): List<PostTag>

    @Query("SELECT * FROM post_tags WHERE id LIKE :id")
    suspend fun getTag(id: Int): PostTag

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(list: List<PostTag>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: PostTag)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTag(tag: PostTag)

    @Query("DELETE FROM post_tags")
    suspend fun clearTags()
}