package com.kryak.database.di

import android.content.Context
import androidx.room.Room
import com.kryak.database.AppDatabase
import com.kryak.database.DownloadDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providesDownloadDao(appDatabase: AppDatabase): DownloadDAO {
        return appDatabase.downloadDao()
    }

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "gitdb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
