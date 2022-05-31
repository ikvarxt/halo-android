package me.ikvarxt.halo.ui.custompages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.HaloJournalType
import me.ikvarxt.halo.repository.JournalsRepository
import javax.inject.Inject

@HiltViewModel
class CustomPagesViewModel @Inject constructor(
    private val journalsRepository: JournalsRepository
) : ViewModel() {

    fun createJournal(content: String, type: HaloJournalType) {
        viewModelScope.launch {
            journalsRepository.createJournal(content, type)
        }
    }
}