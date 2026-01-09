package com.lokal.mume.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lokal.mume.R
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.presentation.utils.CircleContainer
import com.lokal.mume.presentation.utils.CircleShimmerPlaceholder
import com.lokal.mume.presentation.utils.SquircleShapeContainer
import com.lokal.mume.presentation.utils.TextShimmerPlaceholder
import com.lokal.mume.presentation.utils.UiState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.artistState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val elementSize =screenHeightDp/7

    LaunchedEffect(Unit) {
        viewModel.getArtist(
            query = "a",
            limit = 10
        )
    }


    val items = listOf(
        SquircleItem(1, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(2, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(3, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(4, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(5, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(6, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(7, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(8, painterResource(id = R.drawable.ic_mume_app)),
        SquircleItem(9, painterResource(id = R.drawable.ic_mume_app))
    )
    Column(
        modifier = modifier
            .wrapContentSize()
    ) {
        SectionWithTitle(
            title = "Recently Played",
            onSeeAll = {}
        ) {
            LazyRow(
                modifier = Modifier.wrapContentWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    SquircleShapeContainer(
                        image = item.image,
                        size = elementSize,
                        cornerRadius = elementSize/3
                    )
                }
            }
        }

        SectionWithTitle(
            title = "Artists",
            onSeeAll = {}
        ) {
            when (state) {
                UiState.Nothing -> {
                }

                UiState.Loading -> {
                    LazyRow(
                        modifier = Modifier.wrapContentWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(10) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircleShimmerPlaceholder(size = elementSize)
                                Spacer(modifier = Modifier.height(5.dp))
                                TextShimmerPlaceholder()
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    val artists =
                        (state as UiState.Success<List<ArtistResult>>).data

                    LazyRow(
                        modifier = Modifier.wrapContentWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(artists) { artist ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircleContainer(
                                    artist = artist,
                                    size = elementSize
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(artist.name, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = (state as UiState.Error).message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }

        }

        SectionWithTitle(
            title = "Most Played",
            onSeeAll = {}
        ) {
            LazyRow(
                modifier = Modifier.wrapContentWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    SquircleShapeContainer(
                        image = item.image,
                        size = 80.dp,
                        cornerRadius = 24.dp
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // start point
        SectionWithTitle(
            title = "Artists",
            onSeeAll = {}
        ) {
            when (state) {
                UiState.Nothing -> {
                }

                UiState.Loading -> {
                    LazyRow(
                        modifier = Modifier.wrapContentWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(10) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircleShimmerPlaceholder(size = elementSize)
                                Spacer(modifier = Modifier.height(5.dp))
                                TextShimmerPlaceholder()
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    val artists =
                        (state as UiState.Success<List<ArtistResult>>).data

                    LazyRow(
                        modifier = Modifier.wrapContentWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(artists) { artist ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircleContainer(
                                    artist = artist,
                                    size = elementSize
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(artist.name, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = (state as UiState.Error).message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }

        }
        SectionWithTitle(
                title = "Artists",
        onSeeAll = {}
        ) {
        when (state) {
            UiState.Nothing -> {
            }

            UiState.Loading -> {
                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(10) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircleShimmerPlaceholder(size = elementSize)
                            Spacer(modifier = Modifier.height(5.dp))
                            TextShimmerPlaceholder()
                        }
                    }
                }
            }

            is UiState.Success -> {
                val artists =
                    (state as UiState.Success<List<ArtistResult>>).data

                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artists) { artist ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircleContainer(
                                artist = artist,
                                size = elementSize
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(artist.name, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = (state as UiState.Error).message,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }

        }
        SectionWithTitle(
            title = "Artists",
        onSeeAll = {}
        ) {
        when (state) {
            UiState.Nothing -> {
            }

            UiState.Loading -> {
                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(10) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircleShimmerPlaceholder(size = elementSize)
                            Spacer(modifier = Modifier.height(5.dp))
                            TextShimmerPlaceholder()
                        }
                    }
                }
            }

            is UiState.Success -> {
                val artists =
                    (state as UiState.Success<List<ArtistResult>>).data

                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artists) { artist ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircleContainer(
                                artist = artist,
                                size = elementSize
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(artist.name, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = (state as UiState.Error).message,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }

        }
        SectionWithTitle(
            title = "Artists",
        onSeeAll = {}
        ) {
        when (state) {
            UiState.Nothing -> {
            }

            UiState.Loading -> {
                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(10) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircleShimmerPlaceholder(size = elementSize)
                            Spacer(modifier = Modifier.height(5.dp))
                            TextShimmerPlaceholder()
                        }
                    }
                }
            }

            is UiState.Success -> {
                val artists =
                    (state as UiState.Success<List<ArtistResult>>).data

                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artists) { artist ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircleContainer(
                                artist = artist,
                                size = elementSize
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(artist.name, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = (state as UiState.Error).message,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }

        }
        SectionWithTitle(
            title = "Artists",
        onSeeAll = {}
        ) {
        when (state) {
            UiState.Nothing -> {
            }

            UiState.Loading -> {
                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(10) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircleShimmerPlaceholder(size = elementSize)
                            Spacer(modifier = Modifier.height(5.dp))
                            TextShimmerPlaceholder()
                        }
                    }
                }
            }

            is UiState.Success -> {
                val artists =
                    (state as UiState.Success<List<ArtistResult>>).data

                LazyRow(
                    modifier = Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artists) { artist ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircleContainer(
                                artist = artist,
                                size = elementSize
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(artist.name, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = (state as UiState.Error).message,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }

    }
        //end POint
    }
}


data class SquircleItem(
    val id: Int,
    val image: Painter
)