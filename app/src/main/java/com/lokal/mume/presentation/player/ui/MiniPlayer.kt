package com.lokal.mume.presentation.player.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lokal.mume.playback.PlaybackState
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.utils.CustomHorizontalDivider
import com.lokal.mume.presentation.utils.SquircleShapeContainer
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniPlayer(
    state: PlaybackState,
    onExpand: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    playerViewModel: PlayerViewModel,
    onDismiss: () -> Unit
) {
    val song = state.currentSong ?: return

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 40) onDismiss() // Swipe down to clear
                    }
                }
                .clickable { onExpand() },
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            tonalElevation = 8.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                CustomHorizontalDivider(
                    modifier = Modifier.align(Alignment.TopCenter),
                    thickness = 0.5.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SquircleShapeContainer(
                        song = song,
                        size = 48.dp,
                        cornerRadius = 12.dp,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState(key = "artwork_${song.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(contentScale = ContentScale.FillBounds),
                            clipInOverlayDuringTransition = OverlayClip(
                                SquircleShape(
                                    radius = 12.dp,
                                    cornerSmoothing = CornerSmoothing.High
                                )
                            )
                        )
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.sharedBounds(
                                rememberSharedContentState(key = "title_${song.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                            )
                        )
                        Text(
                            text = song.artist,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = {
                        if (state.isPlaying) playerViewModel.pause() else playerViewModel.play(song)
                    }) {
                        Icon(
                            imageVector =
                                if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(onClick = { playerViewModel.playNext() }) {
                        Icon(
                            Icons.Filled.SkipNext,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
