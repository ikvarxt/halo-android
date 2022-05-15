package me.ikvarxt.halo.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "post_item")
data class PostItem(
    @PrimaryKey
    val id: Int,
    val title: String,
    val slug: String,
    @ColumnInfo(name = "create_time")
    val createTime: Calendar,
    @ColumnInfo(name = "update_time")
    val updateTime: Calendar,
    @ColumnInfo(name = "full_path")
    val fullPath: String,
    val status: PostStatus,
    val metaDescription: String?,
    val metaKeyWords: String?,
    val editorType: PostEditorType = PostEditorType.MARKDOWN,
    @ColumnInfo(name = "edit_time")
    val editTime: Calendar,
    // upper fields are the minimal fields that PostItem have

    val summary: String?,
    // return could be empty string
    val thumbnail: String,
    @ColumnInfo(name = "in_progress")
    val inProgress: Boolean,
    // return could be empty string
    val template: String,
    @ColumnInfo(name = "top_priority")
    val topPriority: Int,
    val topped: Boolean,
    // 需要每次重新获取的值
    val visits: Int,
    @ColumnInfo(name = "word_count")
    val wordCount: Int,
    val password: String?,
    val likes: Int = 0,
    @ColumnInfo(name = "disallow_comment")
    val disallowComment: Boolean
)
