package me.ikvarxt.halo.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideMarkwon(application: Application): Markwon = Markwon.builder(application)
        .usePlugin(GlideImagesPlugin.create(application))
        .usePlugin(TablePlugin.create(application))
        .build()

    @Singleton
    @Provides
    fun provideMarkwonEditor(markwon: Markwon): MarkwonEditor = MarkwonEditor.create(markwon)

}