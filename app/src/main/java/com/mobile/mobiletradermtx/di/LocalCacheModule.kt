package com.mobile.mobiletradermtx.di

import android.app.Application
import androidx.room.Room
import com.mobile.mobiletradermtx.dao.AppDatabase
import com.mobile.mobiletradermtx.datasource.AppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalCacheModule {

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase): AppDao {
        return db.appdao
    }

}