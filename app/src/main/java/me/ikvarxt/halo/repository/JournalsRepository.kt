package me.ikvarxt.halo.repository

import androidx.room.withTransaction
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.HaloJournal
import me.ikvarxt.halo.entites.HaloJournalType
import me.ikvarxt.halo.entites.network.JournalRequestBody
import me.ikvarxt.halo.extentions.showNetworkErrorToast
import me.ikvarxt.halo.network.JournalApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalsRepository @Inject constructor(
    private val service: JournalApiService,
    private val db: HaloDatabase
) {
    private val dao = db.journalsDao()

    val journals = dao.getAllJournalsFlow()

    suspend fun listAllJournals() {
        val result = service.getLatestJournals()

        when (result) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.insertJournals(result.data)
                }
            }
            is NetworkResult.Failure -> {
                showNetworkErrorToast(result.msg)
            }
        }
    }

    suspend fun updateJournal(
        journal: HaloJournal,
        content: String,
        type: HaloJournalType
    ) {
        val body = JournalRequestBody(content, type)

        val result = service.updateJournal(journal.id, body)

        when (result) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.insertJournal(result.data)
                }
            }
            is NetworkResult.Failure -> showNetworkErrorToast(result.msg)
        }
    }

    suspend fun deleteJournal(journal: HaloJournal) {
        when (val result = service.deleteJournal(journal.id)) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.deleteJournal(journal)
                }
            }
            is NetworkResult.Failure -> showNetworkErrorToast(result.msg)
        }
    }

    suspend fun createJournal(content: String, type: HaloJournalType) {
        val body = JournalRequestBody(content, type)
        when (val result = service.createJournal(body)) {
            is NetworkResult.Success -> {
                db.withTransaction {
                    dao.insertJournal(result.data)
                }
            }
            is NetworkResult.Failure -> showNetworkErrorToast(result.msg)
        }
    }
}