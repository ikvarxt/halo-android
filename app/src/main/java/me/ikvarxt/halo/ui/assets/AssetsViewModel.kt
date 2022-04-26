package me.ikvarxt.halo.ui.assets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ikvarxt.halo.repository.AttachmentsRepository
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(
    repository: AttachmentsRepository
) : ViewModel() {

    val attachments = repository.getAttachments()
}