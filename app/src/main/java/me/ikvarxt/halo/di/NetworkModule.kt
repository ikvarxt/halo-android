package me.ikvarxt.halo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ikvarxt.halo.network.Constants
import me.ikvarxt.halo.network.ContentApiService
import me.ikvarxt.halo.network.converters.CalendarGsonConverter
import me.ikvarxt.halo.network.okhttp.ContentApiHeadersInterceptor
import me.ikvarxt.halo.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Calendar::class.java, CalendarGsonConverter())
            .create()
    }

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ContentApiHeadersInterceptor())
            .build()
    }

    @Provides
    fun provideContentApiService(gson: Gson, client: OkHttpClient): ContentApiService {
        return Retrofit.Builder()
            .baseUrl("${Constants.domain}/api/content/")
            .client(client)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ContentApiService::class.java)
    }
}