package me.ikvarxt.halo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.ikvarxt.halo.dao.*
import me.ikvarxt.halo.entites.*

@Database(
    entities = [
        PostItem::class,
        PostDetails::class,
        PostTag::class,
        PostCategory::class,
        HaloOption::class,
        HaloJournal::class,
        UserProfile::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HaloDatabase : RoomDatabase() {

    abstract fun postItemDao(): PostItemDao
    abstract fun postDetailsDao(): PostDetailsDao
    abstract fun postTagsDao(): PostTagsDao
    abstract fun postCategoriesDao(): PostCategoriesDao

    abstract fun optionsDao(): OptionsDao

    abstract fun journalsDao(): JournalsDao

    abstract fun userProfileDao(): UserProfileDao
}