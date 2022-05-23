package me.ikvarxt.halo.entites

import com.google.gson.annotations.SerializedName
import me.ikvarxt.halo.di.NetworkModule
import java.util.*

data class Attachment(
    val id: Int,
    val name: String,
    val createTime: Calendar,
    val fileKey: String,
    val height: Int,
    val width: Int,
    @SerializedName("path")
    private val endPath: String,
    val size: Long,
    val suffix: String,
    @SerializedName("thumbPath")
    private val endThumbPath: String,
//    val type
) {
    val path: String
        get() = if (isEndPointPath(endPath)) {
            "${NetworkModule.PLACEHOLDER_DOMAIN}$endPath"
        } else endPath

    val thumbnailPath: String
        get() = if (isEndPointPath(endThumbPath)) {
            "${NetworkModule.PLACEHOLDER_DOMAIN}$endThumbPath"
        } else endThumbPath

    private fun isEndPointPath(path: String): Boolean {
        return path.startsWith("/")
    }
}

