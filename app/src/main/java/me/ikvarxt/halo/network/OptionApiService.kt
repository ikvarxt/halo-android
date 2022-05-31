package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.HaloOption
import me.ikvarxt.halo.entites.network.SaveOptionBody
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OptionApiService {

    @GET("options")
    suspend fun listsAllOptions(): NetworkResult<List<HaloOption>>

    @POST("options/saving")
    suspend fun saveOptions(
        @Body body: List<SaveOptionBody>
    ): NetworkResult<Unit>
}