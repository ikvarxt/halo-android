package me.ikvarxt.halo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import me.ikvarxt.halo.network.Constants
import me.ikvarxt.halo.network.ContentApiService
import me.ikvarxt.halo.utils.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContentApiService(): ContentApiService {
        return Retrofit.Builder()
            .baseUrl("${Constants.domain}/api/")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContentApiService::class.java)
    }
}