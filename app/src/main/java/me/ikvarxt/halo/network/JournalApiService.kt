package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.HaloJournal
import me.ikvarxt.halo.entites.network.JournalRequestBody
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.*

interface JournalApiService {

    @GET("journals/latest")
    suspend fun getLatestJournals(
        @Query("top") top: Int = 20
    ): NetworkResult<List<HaloJournal>>

    @POST("journals")
    suspend fun createJournal(
        @Body body: JournalRequestBody
    ): NetworkResult<HaloJournal>

    @PUT("journals/{id}")
    suspend fun updateJournal(
        @Path("id") journalId: Int,
        @Body body: JournalRequestBody
    ): NetworkResult<HaloJournal>

    @DELETE("journals/{id}")
    suspend fun deleteJournal(
        @Path("id") journalId: Int
    ): NetworkResult<HaloJournal>
}