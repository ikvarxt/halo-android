package me.ikvarxt.halo.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "post_categories")
data class PostCategory(
    @PrimaryKey
    val id: Int,
    val name: String,
    val slug: String,
    @ColumnInfo(name = "create_time")
    val createTime: Calendar,
    val description: String,
    @ColumnInfo(name = "parent_id")
    val parentId: Int,
    val priority: Int,
    val thumbnail: String,
    val password: String?,
    @ColumnInfo(name = "full_path")
    val fullPath: String,
)
