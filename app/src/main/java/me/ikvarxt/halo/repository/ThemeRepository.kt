package me.ikvarxt.halo.repository

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.entites.HaloThemeSettings
import me.ikvarxt.halo.entites.ThemeConfigurationGroup
import me.ikvarxt.halo.extentions.showNetworkErrorToast
import me.ikvarxt.halo.network.ThemeApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val service: ThemeApiService,
    private val gson: Gson
) {

    suspend fun getAllThemes(): NetworkResult<List<HaloTheme>> = service.getAllTheme()

    suspend fun activateTheme(id: String): NetworkResult<HaloTheme> = service.activateTheme(id)

    fun fetchActivatedThemeConfig(): Flow<List<ThemeConfigurationGroup>?> = flow {
        val result = service.fetchActivatedThemeConfig()
        val data = (result as? NetworkResult.Success)?.data

        emit(data)
    }

    suspend fun saveThemeSettings(optionsMap: Map<String, String>) {
        val jsonData = gson.toJson(optionsMap)
        val body = RequestBody.create(MediaType.parse("application/json"), jsonData)
        when (val result = service.savesThemeSettings(body)) {
            is NetworkResult.Success -> {
                // TODO: add some operation
            }
            is NetworkResult.Failure -> showNetworkErrorToast(result.msg)

        }
    }

    suspend fun listsThemeSettings(id: String): List<HaloThemeSettings> {
//        val result = service.listThemeSettingsById(id)
//        when (result) {
//            is NetworkResult.Success -> {
//                return result.data
//            }
//            is NetworkResult.Failure -> {
//                showNetworkErrorToast(result.msg)
//                return emptyList()
//            }
//        }
        return emptyList()
    }

    suspend fun listsActivatedThemeSettings(): List<HaloThemeSettings> {
        val response = service.listActivatedThemeSettings()

        val list = mutableListOf<HaloThemeSettings>()

        if (response.isSuccessful) {
            val body = response.body() as? LinkedTreeMap<String, *> ?: return emptyList()

            body.forEach { (k, v) ->
                list.add(HaloThemeSettings(k, v.toString()))
            }

            return list
        } else {
            return emptyList()
        }
    }
}