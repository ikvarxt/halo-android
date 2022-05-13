package me.ikvarxt.halo.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideMarkwon(application: Application): Markwon = Markwon.create(application)

    @Singleton
    @Provides
    fun provideMarkwonEditor(markwon: Markwon): MarkwonEditor = MarkwonEditor.create(markwon)

}