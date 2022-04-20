package me.ikvarxt.halo.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import me.ikvarxt.halo.entites.PostItem

@Dao
interface PostItemDao {

    @Query("SELECT * FROM post_item ORDER BY create_time DESC")
    fun loadAllPosts(): LiveData<List<PostItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostItem(items: List<PostItem>)

    @Query("DELETE FROM post_item")
    suspend fun clearAll()

    @Query("SELECT * FROM post_item")
    fun pagingSource(): PagingSource<Int, PostItem>
}