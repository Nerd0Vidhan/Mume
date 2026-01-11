package com.lokal.mume.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lokal.mume.data.database.dao.SongDao
import com.lokal.mume.data.database.entity.SongEntity

@Database(entities = [SongEntity::class], version = 2)
abstract class MumeDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}
