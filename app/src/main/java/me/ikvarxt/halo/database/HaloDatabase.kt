package me.ikvarxt.halo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.ikvarxt.halo.dao.PostDetailsDao
import me.ikvarxt.halo.dao.PostItemDao
import me.ikvarxt.halo.dao.PostTagsDao
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.entites.PostTag

@Database(
    entities = [PostItem::class, PostDetails::class, PostTag::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HaloDatabase : RoomDatabase() {

    abstract fun postItemDao(): PostItemDao

    abstract fun postDetailsDao(): PostDetailsDao

    abstract fun postTagsDao(): PostTagsDao
}