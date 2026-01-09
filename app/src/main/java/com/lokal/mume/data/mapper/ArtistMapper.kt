package com.lokal.mume.data.mapper

import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.domain.model.ArtistDetails

fun ArtistResult.toDomain(): ArtistDetails {
    return ArtistDetails(
        id = this.id,
        name = this.name,
        role = this.role,
        image = this.image, // Ensure Domain model handles List<ArtistImage> or map it too
        type = this.type,
        url = this.url
    )
}