package me.ikvarxt.halo.ui.custompages.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.HaloJournal
import me.ikvarxt.halo.entites.HaloJournalType
import me.ikvarxt.halo.repository.JournalsRepository
import javax.inject.Inject

@HiltViewModel
class JournalsViewModel @Inject constructor(
    private val repository: JournalsRepository
) : ViewModel() {

    val journals = repository.journals

    init {
        viewModelScope.launch {
            repository.listAllJournals()
        }
    }

    fun updateJournal(journal: HaloJournal, content: String, type: HaloJournalType) {
        viewModelScope.launch {
            repository.updateJournal(journal, content, type)
        }
    }

    fun deleteJournal(journal: HaloJournal) {
        viewModelScope.launch {
            repository.deleteJournal(journal)
        }
    }
}