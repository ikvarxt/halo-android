package me.ikvarxt.halo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ikvarxt.halo.entites.PostItem

@Dao
interface PostItemDao {

    @Query("SELECT * FROM post_item ORDER BY create_time DESC")
    fun loadAllPosts(): LiveData<List<PostItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPostItem(vararg item: PostItem)

}