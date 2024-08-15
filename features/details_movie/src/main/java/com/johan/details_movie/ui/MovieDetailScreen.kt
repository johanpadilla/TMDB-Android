package com.johan.details_movie.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.johan.details_movie.R
import com.johan.details_movie.model.MovieDetailState
import com.johan.details_movie.ui.composables.Genres
import com.johan.details_movie.ui.composables.ProductionCompany
import com.johan.details_movie.ui.composables.ProductionCountry
import com.johan.details_movie.ui.composables.SpokenLanguages
import com.johan.shared.ui.CenteredMessage
import com.johan.shared.utils.formatDateToMonthAndYear
import com.johan.shared.utils.toMoneyFormat

@Composable
fun MovieDetailScreen(
    onBackPressed: () -> Unit,
    movieId: String?,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.detail.collectAsStateWithLifecycle(lifecycle = LocalLifecycleOwner.current.lifecycle)

    MovieDetailContainer(movieDetailState = state, onBackPressed, viewModel, movieId)

    LaunchedEffect(Unit) {
        viewModel.getMovieDetail(movieId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailContainer(
    movieDetailState: MovieDetailState,
    onBackPressed: () -> Unit,
    viewModel: MovieDetailsViewModel,
    movieId: String?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.movie_details_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back_accessibility)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = movieDetailState is MovieDetailState.Loading,
            onRefresh = {
                viewModel.onRefresh(movieId)
            },
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    end = dimensionResource(
                        id = R.dimen.smallHorizontalPadding
                    ),
                    start = dimensionResource(id = R.dimen.smallHorizontalPadding),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            Content(movieDetailState = movieDetailState)
        }

    }
}

@Composable
private fun Content(movieDetailState: MovieDetailState) {
    when (movieDetailState) {
        is MovieDetailState.Loaded -> {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallVerticalPadding))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding))
                ) {
                    MovieDetailPoster(url = movieDetailState.movieDetail?.fullPosterPath)
                    Column {
                        val releasedDate =
                            movieDetailState.movieDetail?.releaseDate?.let { "(${it.formatDateToMonthAndYear()})" }
                        Title(movieDetailState.movieDetail?.originalTitle, releasedDate)

                        Genres(genres = movieDetailState.movieDetail?.genres)

                        if (movieDetailState.movieDetail?.spokenLanguages != null && movieDetailState.movieDetail.spokenLanguages.isNotEmpty()) {
                            SpokenLanguages(movieDetailState.movieDetail.spokenLanguages)
                        }
                        if (movieDetailState.movieDetail?.budget != null && movieDetailState.movieDetail.budget > 0) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
                            Text(text = "${stringResource(id = R.string.budget_text)}: $${movieDetailState.movieDetail.budget.toMoneyFormat()}")
                        }

                        movieDetailState.movieDetail?.status.let {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
                            Text(text = "${stringResource(id = R.string.status_text)}: $it")
                        }
                    }

                }

                movieDetailState.movieDetail?.overview?.let { overview ->
                    val synopsis = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${stringResource(id = R.string.synopsis_text)} ")
                        }
                        append(overview)
                    }
                    Text(text = synopsis)
                }

                movieDetailState.movieDetail?.productionCompanies?.let { companies ->
                    if (companies.any { it.logoPath != null }) ProductionCompany(companies = companies)
                }

                movieDetailState.movieDetail?.productionCountries?.let { countries ->
                    ProductionCountry(countries = countries)
                }
            }
        }

        is MovieDetailState.Loading -> CenteredMessage(message = stringResource(id = R.string.loading_text_message))
        is MovieDetailState.Empty -> CenteredMessage(message = stringResource(id = R.string.empty_text_message))
        is MovieDetailState.Error -> CenteredMessage(message = stringResource(id = R.string.error_text_message))
    }
}

@Composable
private fun Title(originalTitle: String?, releasedDate: String?) {
    originalTitle?.let {
        Text(
            text = "$it $releasedDate",
            textAlign = TextAlign.Center,
            //style = titleLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    }
}


@Composable
private fun MovieDetailPoster(url: String?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url).crossfade(true)
            .build(),
        contentDescription = null,
    )
}