package me.ikvarxt.halo.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.entites.ThemeConfigurationGroup
import me.ikvarxt.halo.network.ThemeApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val service: ThemeApiService
) {

    suspend fun getAllThemes(): NetworkResult<List<HaloTheme>> = service.getAllTheme()

    suspend fun activateTheme(id: String): NetworkResult<HaloTheme> = service.activateTheme(id)

    fun fetchActivatedThemeConfig(): Flow<List<ThemeConfigurationGroup>?> = flow {
        val result = service.fetchActivatedThemeConfig()
        val data = (result as? NetworkResult.Success)?.data

        emit(data)
    }

}