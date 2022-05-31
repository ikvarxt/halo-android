package me.ikvarxt.halo.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.entites.HaloThemeSettings
import me.ikvarxt.halo.entites.ThemeConfigurationGroup
import me.ikvarxt.halo.network.ThemeApiService
import me.ikvarxt.halo.network.infra.NetworkResult
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

    suspend fun saveThemeSettings(key: String, value: String) {

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
            val body = response.body().toString()

            // FIXME: cant work
//            val jsonObject = JSONObject(body)

//           for (key in jsonObject.keys()){
//               list.add(HaloThemeSettings(key,jsonObject.getString(key)))
//           }

//            val data = gson.newJsonReader(StringReader(body)).apply {
//                isLenient = true
//            }
//            data.beginObject()
//            while (data.hasNext()) {
//                val key = data.nextName()
//
//                val token = data.peek()
//
//                val value = when (token) {
//                    JsonToken.STRING -> data.nextString()
//                    JsonToken.BOOLEAN -> data.nextBoolean().toString()
//                    JsonToken.NUMBER -> data.nextInt().toString()
//                    else -> ""
//                }
//
//                Log.d("TAG", "listsActivatedThemeSettings: next string:: $key $value")
//            }
//            data.endObject()

//            for (set in data.asJsonArray) {
//                   list.add(HaloThemeSettings(set.key,set.value.asString))
//            }
//            Log.d("TAG", "listsActivatedThemeSettings: $data")
            return emptyList()
        } else {
            return emptyList()
        }
    }

}