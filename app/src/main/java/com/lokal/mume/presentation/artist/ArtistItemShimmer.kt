package com.lokal.mume.presentation.artist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lokal.mume.presentation.utils.shimmer

@Composable
fun ArtistItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular Image Shimmer
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .shimmer(CircleShape)
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 20.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmer()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 14.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmer()
            )
        }
    }
}