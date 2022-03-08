package me.ikvarxt.halo.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import me.ikvarxt.halo.application
import me.ikvarxt.halo.dao.PostDetailsDao
import me.ikvarxt.halo.dao.PostItemDao
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.network.Constants
import me.ikvarxt.halo.network.ContentApiService
import me.ikvarxt.halo.network.converters.CalendarGsonConverter
import me.ikvarxt.halo.utils.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Calendar::class.java, CalendarGsonConverter())
            .create()
    }

    @Provides
    fun provideContentApiService(gson: Gson): ContentApiService {
        return Retrofit.Builder()
            .baseUrl("${Constants.domain}/api/")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ContentApiService::class.java)
    }

    @Provides
    fun provideDatabase(): HaloDatabase {
        return Room.databaseBuilder(
            application,
            HaloDatabase::class.java, "halo-db"
        ).build()
    }

    @Provides
    fun providePostItemDao(database: HaloDatabase): PostItemDao {
        return database.postItemDao()
    }

    @Provides
    fun providePostDetailDao(database: HaloDatabase) = database.postDetailsDao()

}