package com.johan.popular_movies.ui.composables

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.johan.popular_movies.model.PopularMovie

@Composable
fun MovieCard(modifier: Modifier = Modifier, popularMovie: PopularMovie, onPopularMovieClick: (String) -> Unit) {
    Card(modifier = modifier, onClick = { onPopularMovieClick.invoke(popularMovie.id.toString()) }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(popularMovie.fullPosterUrl).crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}