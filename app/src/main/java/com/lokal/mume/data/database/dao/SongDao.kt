package com.lokal.mume.data.database.dao

import androidx.room.*
import com.lokal.mume.data.database.entity.SongEntity

@Dao
interface SongDao {

    @Query("SELECT * FROM songs ORDER BY playedAt DESC LIMIT 30")
    suspend fun lastPlayed(): List<SongEntity>

    @Query("SELECT * FROM songs WHERE isInQueue = 1 ORDER BY playedAt ASC")
    suspend fun getQueue(): List<SongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: SongEntity)

    @Query("DELETE FROM songs WHERE id NOT IN (SELECT id FROM songs ORDER BY playedAt DESC LIMIT 30)")
    suspend fun trimHistory()

    @Query("DELETE FROM songs WHERE isInQueue = 0 AND playedAt < :time")
    suspend fun cleanup(time: Long)

    @Query("""
SELECT * FROM songs 
WHERE isInQueue = 0 
ORDER BY playedAt DESC
""")
    suspend fun getHistory(): List<SongEntity>

    @Query("UPDATE songs SET isInQueue = 0 WHERE id = :songId")
    suspend fun removeFromQueue(songId: String)

    @Query("UPDATE songs SET isInQueue = 0")
    suspend fun clearQueue()

    @Query("UPDATE songs SET lastPosition = :position, playedAt = :timestamp WHERE id = :songId")
    suspend fun updateProgress(songId: String, position: Long, timestamp: Long)

}
