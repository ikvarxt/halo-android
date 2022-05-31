package me.ikvarxt.halo.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.ikvarxt.halo.entites.HaloJournal

@Dao
interface JournalsDao {
    @Query("SELECT * FROM halo_journals ORDER BY create_time DESC")
    suspend fun getAllJournals(): List<HaloJournal>

    @Query("SELECT * FROM halo_journals ORDER BY create_time DESC")
    fun getAllJournalsFlow(): Flow<List<HaloJournal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournals(list: List<HaloJournal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournal(journal: HaloJournal)

    @Delete
    fun deleteJournal(journal: HaloJournal)
}