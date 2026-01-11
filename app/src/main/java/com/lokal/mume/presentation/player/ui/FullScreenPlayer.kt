package com.lokal.mume.presentation.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.utils.formatTime
import sv.lib.squircleshape.SquircleShape

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FullPlayerScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: PlayerViewModel = hiltViewModel(),
    onCollapse: () -> Unit
) {
    val state by viewModel.playbackState.collectAsState()
    val song = state.currentSong ?: return

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

// Sync local slider with service position when NOT dragging
    LaunchedEffect(state.position,state.duration) {
        if (!isDragging) {
            sliderPosition = state.position.toFloat()
        }
        if (state.duration > 0 && state.position >= state.duration) {
            viewModel.playNext()
        }
    }

    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 60) onCollapse()
                    }
                }
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCollapse) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            AsyncImage(
                model = song.artwork,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(SquircleShape(40.dp))
                    .sharedBounds(
                        rememberSharedContentState(key = "artwork_${song.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(contentScale = ContentScale.FillBounds),
                        clipInOverlayDuringTransition = OverlayClip(SquircleShape(40.dp))
                    )
            )

            Spacer(modifier = Modifier.weight(0.1f))

            animatedVisibilityScope.RenderFadeInContent {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState(key = "title_${song.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = song.artist,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            animatedVisibilityScope.RenderFadeInContent {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                    Slider(
                        value = sliderPosition,
                        onValueChange = {
                            isDragging = true
                            sliderPosition = it
                        },
                        onValueChangeFinished = {
                            isDragging = false
                            viewModel.seekTo(sliderPosition)
                        },
                        valueRange = 0f..state.duration.toFloat().coerceAtLeast(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Current Time Label
                        Text(
                            text = sliderPosition.toLong().formatTime(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // Total Duration Label
                        Text(
                            text = state.duration.formatTime(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            animatedVisibilityScope.RenderFadeInContent {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.playPrevious() }) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = null)
                    }
                    IconButton(onClick = { viewModel.seekBackward() }) {
                        Icon(Icons.Default.Replay10, contentDescription = null)
                    }

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(72.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (state.isPlaying) viewModel.pause() else viewModel.play(song)
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    IconButton(onClick = { viewModel.seekForward() }) {
                        Icon(Icons.Filled.Forward10, contentDescription = null)
                    }
                    IconButton(onClick = { viewModel.playNext() }) {
                        Icon(Icons.Filled.SkipNext, contentDescription = null)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            animatedVisibilityScope.RenderFadeInContent {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null)
                    Icon(Icons.Outlined.Timer, contentDescription = null)
                    Icon(Icons.Filled.Cast, contentDescription = null)
                    Icon(Icons.Filled.MoreVert, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            animatedVisibilityScope.RenderFadeInContent {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                    Text("Lyrics", style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AnimatedVisibilityScope.RenderFadeInContent(
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        content()
    }
}