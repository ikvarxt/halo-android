package me.ikvarxt.halo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ikvarxt.halo.account.AccountManager
import me.ikvarxt.halo.extentions.create
import me.ikvarxt.halo.network.*
import me.ikvarxt.halo.network.converters.CalendarGsonConverter
import me.ikvarxt.halo.network.okhttp.AdminApiHeadersInterceptor
import me.ikvarxt.halo.utils.NetworkResultCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        const val HTTP_SCHEMA = "https://"
        const val PLACEHOLDER_HOST = "halo.placeholder"
        const val PLACEHOLDER_DOMAIN = HTTP_SCHEMA + PLACEHOLDER_HOST

        private const val ADMIN_API_END_POINT = "/api/admin/"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Calendar::class.java, CalendarGsonConverter())
            .create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(accountManager: AccountManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AdminApiHeadersInterceptor(accountManager))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("$HTTP_SCHEMA$PLACEHOLDER_HOST$ADMIN_API_END_POINT")
            .client(client)
            .addCallAdapterFactory(NetworkResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideAdminApiService(retrofit: Retrofit): AdminApiService = retrofit.create()

    @Provides
    @Singleton
    fun providePostApiService(retrofit: Retrofit): PostApiService = retrofit.create()

    @Provides
    @Singleton
    fun provideAttachmentApiService(retrofit: Retrofit): AttachmentApiService = retrofit.create()

    @Provides
    @Singleton
    fun provideCommentApiService(retrofit: Retrofit): CommentApiService = retrofit.create()

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService = retrofit.create()

    @Provides
    @Singleton
    fun provideThemeApiService(retrofit: Retrofit): ThemeApiService = retrofit.create()

    @Provides
    @Singleton
    fun provideTagAndCategoriesApiService(retrofit: Retrofit): PostTagsAndCategoriesApiService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideOptionApiService(retrofit: Retrofit): OptionApiService = retrofit.create()
}