package com.lokal.mume.data.mapper


import com.lokal.mume.data.database.entity.SongEntity
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.domain.model.SongModel

fun SongModel.toEntity(isInQueue: Boolean) = SongEntity(
    id, title, artist, genre, streamUrl320, artwork,
    playedAt = System.currentTimeMillis(),
    isInQueue = isInQueue
)

fun SongResult.toSongEntity(
    playedAt: Long = System.currentTimeMillis(),
    isInQueue: Boolean = false
): SongEntity {
    return SongEntity(
        id = id,
        title = name,
        artist = artists.primary.joinToString(", ") { it.name },
        genre = language,
        streamUrl = downloadUrl
            .firstOrNull { it.quality == "320kbps" }
            ?.url
            ?: downloadUrl.lastOrNull()?.url.orEmpty(),
        artwork = image.lastOrNull()?.url.orEmpty(),
        playedAt = playedAt,
        isInQueue = isInQueue
    )
}


fun SongEntity.toDomain() = SongModel(
    id, title, artist, genre, streamUrl, artwork
)

fun SongResult.toSongModel(): SongModel {
    return SongModel(
        id = id,
        title = name,
        artist = artists.primary.joinToString(", ") { it.name },
        genre = language,
        streamUrl320 = downloadUrl
            .firstOrNull { it.quality == "320kbps" }
            ?.url
            ?: downloadUrl.lastOrNull()?.url.orEmpty(),
        artwork = image.lastOrNull()?.url.orEmpty()
    )
}

fun SongEntity.toSongModel(): SongModel {
    return SongModel(
        id = id,
        title = title,
        artist = artist,
        genre = genre,
        streamUrl320 = streamUrl,
        artwork = artwork
    )
}

