package me.ikvarxt.halo.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ikvarxt.halo.application
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
}