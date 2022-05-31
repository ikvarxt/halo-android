package me.ikvarxt.halo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.ikvarxt.halo.entites.UserProfile

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profile")
    fun getUserProfile(): Flow<List<UserProfile>>

    @Query("DELETE FROM user_profile")
    suspend fun clearProfile()
}