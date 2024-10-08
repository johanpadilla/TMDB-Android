package com.johan.details_movie.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.johan.shared.ui.PullToRefreshContainer
import com.johan.shared.ui.theme.titleLarge
import com.johan.shared.utils.formatDateToMonthAndYear
import com.johan.shared.utils.toMoneyFormat
import kotlinx.coroutines.launch

@Composable
fun MovieDetailScreen(
    onBackClick: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val movieDetailState by viewModel.detail.collectAsStateWithLifecycle()
    val isLandScapeMode =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    MovieDetailScreenContent(
        onBackClick = onBackClick,
        movieDetailState = movieDetailState,
        isLandScapeMode
    ) { onError ->
        viewModel.onRefresh(
            onError
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailScreenContent(
    onBackClick: () -> Unit,
    movieDetailState: MovieDetailState,
    isLandScapeMode: Boolean,
    onRefresh: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.movie_details_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back_accessibility)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Content(movieDetailState = movieDetailState, isLandScapeMode, onRefresh)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    movieDetailState: MovieDetailState,
    isLandScapeMode: Boolean,
    onRefresh: (Boolean) -> Unit
) {
    when (movieDetailState) {
        is MovieDetailState.Loaded -> {
            val toolTipState = rememberTooltipState()
            val scope = rememberCoroutineScope()
            PullToRefreshContainer(onRefresh = {
                onRefresh(false)
            },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.smallHorizontalPadding)),
                isRefreshing = movieDetailState.isRefreshing,
                content = {
                    TooltipBox(positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(
                        dimensionResource(id = R.dimen.landscapeMovieDetailSpacing)
                    ), tooltip = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = stringResource(id = R.string.go_back_accessibility)
                        )
                    }, state = toolTipState
                    ) {
                        Column(
                            modifier = Modifier
                                .onFocusEvent { if (isLandScapeMode) scope.launch { toolTipState.show() } }
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
                                if (companies.any { it.logoPath != null }) ProductionCompany(
                                    companies = companies
                                )
                            }

                            movieDetailState.movieDetail?.productionCountries?.let { countries ->
                                ProductionCountry(countries = countries)
                            }
                        }
                    }

                }
            )
        }

        is MovieDetailState.Loading -> CenteredMessage(message = stringResource(id = R.string.loading_text_message))
        is MovieDetailState.Empty -> {
            PullToRefreshContainer(
                isRefreshing = false,
                onRefresh = { onRefresh(true) },
                content = {
                    CenteredMessage(message = stringResource(id = R.string.empty_text_message))
                }
            )
        }

        is MovieDetailState.Error -> {
            PullToRefreshContainer(
                modifier = Modifier.testTag("popular_movie_error_container"),
                isRefreshing = false,
                onRefresh = { onRefresh(true) },
                content = {
                    CenteredMessage(message = stringResource(id = R.string.error_text_message))

                }
            )
        }
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

@Composable
private fun Title(originalTitle: String?, releasedDate: String?) {
    originalTitle?.let {
        Text(
            text = "$it $releasedDate",
            textAlign = TextAlign.Center,
            style = titleLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    }
}