package me.ikvarxt.halo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ikvarxt.halo.entites.PostDetails

@Dao
interface PostDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostDetails(vararg item: PostDetails)

    @Query("select * from post_details where id = :id")
    suspend fun loadPostDetailWithId(id: Int): PostDetails
}