package me.ikvarxt.halo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ikvarxt.halo.entites.PostItem

@Dao
interface PostItemDao {

    @Query( "SELECT * from post_item")
    fun loadAllPosts(): LiveData<List<PostItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPostItem(item: PostItem)


}