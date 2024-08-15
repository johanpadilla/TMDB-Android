package com.johan.popular_movies.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.johan.popular_movies.R
import com.johan.popular_movies.model.PopularMovie

@Composable
fun MovieCardRow(movieList: List<PopularMovie>, onPopularMovieClicked: (String) -> Unit) {
    Row(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.smallHorizontalPadding)),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.smallHorizontalPadding)
        )
    ) {
        movieList.map { movie ->
            MovieCard(
                popularMovie = movie,
                onPopularMovieClick = { movieId ->
                    onPopularMovieClicked(movieId)
                }
            )
        }
    }
}