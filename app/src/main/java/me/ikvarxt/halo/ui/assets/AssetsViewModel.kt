package me.ikvarxt.halo.ui.assets

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.Attachment
import me.ikvarxt.halo.repository.AttachmentsRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val repository: AttachmentsRepository
) : ViewModel() {

    val attachments = repository.getAttachments()

    fun publish(uri: Uri) {
        viewModelScope.launch {
            val path = uri.path
            // FIXME: can't get file, lack some permissions
            val file = File(path)
            val requestBody = RequestBody.create(MediaType.parse("multipart/from-data"), file)
            val part: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file.name, requestBody)

            val result = repository.uploadAttachment(part)
        }
    }

    fun deletePermanently(attachment: Attachment) {
        viewModelScope.launch {
            repository.deleteAttachmentPermanently(attachment.id)
        }
    }
}