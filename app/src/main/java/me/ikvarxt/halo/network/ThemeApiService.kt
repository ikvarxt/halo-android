package me.ikvarxt.halo.network

import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.entites.ThemeConfigurationGroup
import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ThemeApiService {

    @GET("themes")
    suspend fun getAllTheme(): NetworkResult<List<HaloTheme>>

    @POST("themes/{themeId}/activation")
    suspend fun activateTheme(@Path("themeId") themeId: String): NetworkResult<HaloTheme>

    @GET("themes/activation/configurations")
    suspend fun fetchActivatedThemeConfig(): NetworkResult<List<ThemeConfigurationGroup>>
}