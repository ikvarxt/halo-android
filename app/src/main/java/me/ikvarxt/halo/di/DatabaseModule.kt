package me.ikvarxt.halo.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ikvarxt.halo.application
import me.ikvarxt.halo.dao.PostItemDao
import me.ikvarxt.halo.database.HaloDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(): HaloDatabase {
        return Room.databaseBuilder(
            application, HaloDatabase::class.java, "halo-db"
        ).build()
    }

    @Provides
    fun providePostItemDao(database: HaloDatabase): PostItemDao = database.postItemDao()

    @Provides
    fun providePostDetailDao(database: HaloDatabase) = database.postDetailsDao()

}