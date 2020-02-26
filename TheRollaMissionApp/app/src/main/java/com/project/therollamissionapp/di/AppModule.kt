package com.project.therollamissionapp.di

import android.app.Application
import androidx.room.Room
import com.project.therollamissionapp.data.source.local.MissionDb
import com.project.therollamissionapp.data.source.local.PatronDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): MissionDb {
        return Room
            .databaseBuilder(app, MissionDb::class.java, "mission.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePatronDao(db: MissionDb): PatronDao {
        return db.patronDao()
    }
}