package me.ikvarxt.halo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.ikvarxt.halo.dao.PostItemDao
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.entites.PostItem

@Database(entities = [PostItem::class, PostDetails::class], version = 1)
@TypeConverters(Converters::class)
abstract class HaloDatabase : RoomDatabase() {
    abstract fun postItemDao(): PostItemDao

}