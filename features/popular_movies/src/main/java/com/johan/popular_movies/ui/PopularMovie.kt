package com.johan.popular_movies.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.johan.popular_movies.model.PopularMovieState

@Composable
fun PopularMoviesContainer(viewModel: PopularMovieViewModel = hiltViewModel()) {
    when (val moviesState = viewModel.movies.collectAsState().value) {
        is PopularMovieState.Loaded -> {
            Text(text = moviesState.movies[0].title ?: "")
        }
        else -> Unit
    }

}