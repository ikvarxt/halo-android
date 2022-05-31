package me.ikvarxt.halo.repository

import androidx.room.withTransaction
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.HaloOption
import me.ikvarxt.halo.entites.network.SaveOptionBody
import me.ikvarxt.halo.extentions.showNetworkErrorToast
import me.ikvarxt.halo.network.OptionApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HaloSettingsRepository @Inject constructor(
    private val service: OptionApiService,
    private val db: HaloDatabase
) {
    private val dao = db.optionsDao()

    val options get() = dao.getAllOptionsFlow()

    suspend fun listAllOptions() {
        when (val result = service.listsAllOptions()) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.insertOptions(result.data)
                }
            }
            is NetworkResult.Failure -> {}
        }
    }

    suspend fun saveOption(option: HaloOption): HaloOption? {
        val result = saveOptions(listOf(option))

        return if (result != null) {
            option
        } else null
    }

    suspend fun saveOptions(list: List<HaloOption>): List<HaloOption>? {
        val body = list.map {
            SaveOptionBody(it.key, it.value)
        }
        val result = service.saveOptions(body)
        when (result) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.insertOptions(list)
                }
            }
            is NetworkResult.Failure -> {
                showNetworkErrorToast(result.msg)
                return null
            }
        }
        return dao.getAllOptions()
    }
}