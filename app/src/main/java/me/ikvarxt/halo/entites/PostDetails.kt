package me.ikvarxt.halo.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_details")
class PostDetails(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "format_content") val formatContent: String?,
    @ColumnInfo(name = "original_content") val originalContent: String?,
    @ColumnInfo(name = "word_count") val wordCount: Int,
    val thumbnail: String?,
    val fullPath: String,
    val slug: String,
    val status: PostStatus,
//    @Ignore
//    val tags: List<PostTag>,
//    @Ignore
//    val categories: List<PostCategory> ,
)