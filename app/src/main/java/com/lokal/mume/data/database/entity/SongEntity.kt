package com.lokal.mume.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val genre: String,
    val streamUrl: String,
    val artwork: String,
    val lastPosition: Long = 0L,
    val playedAt: Long,
    val isInQueue: Boolean
)
