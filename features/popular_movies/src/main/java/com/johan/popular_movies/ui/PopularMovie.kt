package com.johan.popular_movies.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.johan.network.BuildConfig
import com.johan.popular_movies.R
import com.johan.popular_movies.model.PopularMovieState
import com.johan.popular_movies.ui.composables.MovieCardRow
import com.johan.shared.ui.CenteredMessage


const val POPULAR_MOVIE_COLUMNS = 2

@Composable
fun PopularMovieScreen(
    onPopularMovieClicked: (String) -> Unit,
    viewModel: PopularMovieViewModel = hiltViewModel()
) {
    val moviesState by viewModel.movies.collectAsStateWithLifecycle(lifecycle = LocalLifecycleOwner.current.lifecycle)
    PopularMovieContainer(
        onPopularMovieClicked = onPopularMovieClicked,
        viewModel = viewModel,
        moviesState = moviesState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PopularMovieContainer(
    onPopularMovieClicked: (String) -> Unit,
    viewModel: PopularMovieViewModel, moviesState: PopularMovieState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.home_screen_title))
                }
            )
        },

        ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues
                )
                .testTag("popular_movie_container"),
            isRefreshing = moviesState is PopularMovieState.Loading,
            onRefresh = {
                viewModel.restart()
            }
        ) {
            when (moviesState) {
                is PopularMovieState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val chunkedList = moviesState.movies.chunked(POPULAR_MOVIE_COLUMNS)
                        items(chunkedList.size) { index ->
                            MovieCardRow(
                                movieList = chunkedList[index]
                            ) { movieId -> onPopularMovieClicked(movieId) }
                        }
                    }
                }

                is PopularMovieState.Loading -> CenteredMessage(
                    modifier = Modifier.testTag("popular_movie_loading_container"),
                    message = stringResource(id = R.string.loading_text_message)
                )

                is PopularMovieState.Empty -> CenteredMessage(message = stringResource(id = R.string.empty_text_message))
                is PopularMovieState.Error -> CenteredMessage(
                    modifier = Modifier.testTag("popular_movie_error_container"),
                    message = "${stringResource(id = R.string.error_text_message)} ${
                        if (BuildConfig.ACCESS_TOKEN.isEmpty()) stringResource(
                            id = R.string.check_the_token_text
                        ) else ""
                    }"
                )
            }
        }
    }
}
