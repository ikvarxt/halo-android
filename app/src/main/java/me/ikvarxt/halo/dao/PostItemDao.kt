package me.ikvarxt.halo.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ikvarxt.halo.entites.PostItem

@Dao
interface PostItemDao {

    @Query("SELECT * FROM post_item ORDER BY create_time DESC")
    suspend fun loadAllPosts(): List<PostItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PostItem>)

    @Query("DELETE FROM post_item")
    suspend fun clearAll()

    @Query("SELECT * FROM post_item ORDER BY create_time DESC")
    fun pagingSource(): PagingSource<Int, PostItem>

    @Query("DELETE FROM post_item WHERE id LIKE :postId")
    suspend fun deleteById(postId: Int)
}