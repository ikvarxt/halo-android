package me.ikvarxt.halo.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "post_tags")
data class PostTag(
    @PrimaryKey
    val id: Int,
    val name: String,
    val slug: String,
    @ColumnInfo(name = "create_time")
    val createTime: Calendar,
    val color: String,
    val thumbnail: String,
    @ColumnInfo(name = "full_path")
    val fullPath: String,
)