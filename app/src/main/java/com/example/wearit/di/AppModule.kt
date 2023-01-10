package com.example.wearit.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.wearit.data.AppDatabase
import com.example.wearit.data.InternalStorageHelper
import com.example.wearit.data.StoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context
    ) = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "WearIt_DB"
    ).build()

    @Singleton
    @Provides
    fun provideItemDao(appDatabase: AppDatabase) = appDatabase.itemDao()

    @Singleton
    @Provides
    fun provideOutfitDao(appDatabase: AppDatabase) = appDatabase.outfitDao()

    @Singleton
    @Provides
    fun provideStoreSettings(application: Application) = StoreSettings(application.applicationContext)

    @Singleton
    @Provides
    fun provideInternalStorageHelper(application: Application) = InternalStorageHelper(application.applicationContext)
}