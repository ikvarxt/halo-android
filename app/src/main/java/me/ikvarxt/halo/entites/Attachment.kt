package me.ikvarxt.halo.entites

import java.util.*

data class Attachment(
    val id: Int,
    val name: String,
    val createTime: Calendar,
    val fileKey: String,
    val height: Int,
    val width: Int,
    val path: String,
    val size: Long,
    val suffix: String,
    val thumbPath: String,
//    val type
)
