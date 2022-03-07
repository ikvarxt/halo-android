package me.ikvarxt.halo.entites

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

data class ListPostResponse(
    @field:SerializedName("content")
    val content: List<PostItem>,
    val total: Int,
)

@Entity(tableName = "post_item")
data class PostItem(
    @PrimaryKey val id: Long,
    val slug: String,
    val title: String,
    val summary: String,
    @ColumnInfo(name = "create_time") val createTime: Calendar,
    @ColumnInfo(name = "update_time") val updateTime: Calendar,
    @ColumnInfo(name = "full_path") val fullPath: String,
    val thumbnail: String,
)
