package com.lokal.mume.di

import android.content.Context
import androidx.room.Room
import com.lokal.mume.data.database.MumeDatabase
import com.lokal.mume.data.database.dao.SongDao
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
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MumeDatabase {
        return Room.databaseBuilder(
            context,
            MumeDatabase::class.java,
            "mume_db"
        ).build()
    }

    @Provides
    fun provideSongDao(database: MumeDatabase): SongDao {
        return database.songDao()
    }
}
