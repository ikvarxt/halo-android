package me.ikvarxt.halo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.ikvarxt.halo.entites.HaloOption

@Dao
interface OptionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOptions(list: List<HaloOption>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOption(option: HaloOption)

    @Query("SELECT * FROM halo_options")
    fun getAllOptionsFlow(): Flow<List<HaloOption>>

    @Query("SELECT * FROM halo_options")
    suspend fun getAllOptions(): List<HaloOption>

    @Query("SELECT * FROM halo_options WHERE `key` LIKE :optionKey")
    suspend fun getOption(optionKey: String): HaloOption
}