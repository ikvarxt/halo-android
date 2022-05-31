package me.ikvarxt.halo.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "halo_journals")
data class HaloJournal(
    @PrimaryKey
    val id: Int,
    val content: String,
    @ColumnInfo(name = "source_content")
    val sourceContent: String,
    @ColumnInfo(name = "create_time")
    val createTime: Calendar,
    @ColumnInfo(name = "comment_count")
    val commentCount: Int,
    val likes: Int,
    val type: HaloJournalType
)

enum class HaloJournalType {
    PUBLIC, INTIMATE
}
