package me.ikvarxt.halo.ui.assets

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.Attachment
import me.ikvarxt.halo.repository.AttachmentsRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val application: Application,
    private val repository: AttachmentsRepository
) : ViewModel() {

    private val _refreshState = MutableStateFlow(false)
    val refreshState = _refreshState.asStateFlow()

    val attachments = repository.getAttachments()

    fun publish(uri: Uri, fileName: String? = null) {
        viewModelScope.launch {
            val cacheDir = File(application.cacheDir, "upload_image")
            cacheDir.deleteRecursively()
            cacheDir.mkdirs()

            val imageFile = File(cacheDir, "upload").also {
                try {
                    application.contentResolver.openInputStream(uri)!!.writeTo(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val file = File(uri.path)
            val requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)

            val name = if (fileName.isNullOrEmpty() || fileName.isNullOrBlank()) {
                file.name
            } else {
                fileName
            }

            val part: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", name, requestBody)

            val result = repository.uploadAttachment(part)

            _refreshState.emit(true)
        }
    }

    fun deletePermanently(attachment: Attachment) {
        viewModelScope.launch {
            repository.deleteAttachmentPermanently(attachment.id)

            _refreshState.emit(true)
        }
    }

    inline fun <In : InputStream, Out : OutputStream> withStreams(
        inStream: In,
        outStream: Out,
        withBoth: (In, Out) -> Unit
    ) {
        inStream.use { reader ->
            outStream.use { writer ->
                withBoth(reader, writer)
            }
        }
    }

    fun InputStream.copyAndClose(out: OutputStream) = withStreams(this, out) { i, o -> i.copyTo(o) }

    fun InputStream.writeTo(file: File) = copyAndClose(file.outputStream())
}