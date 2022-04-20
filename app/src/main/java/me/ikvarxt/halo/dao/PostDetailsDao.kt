package me.ikvarxt.halo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ikvarxt.halo.entites.PostDetails

@Dao
interface PostDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPostDetails(vararg item: PostDetails)

    @Query("select * from post_details where id = :id")
    fun loadPostDetailWithId(id: Int): LiveData<PostDetails>
}