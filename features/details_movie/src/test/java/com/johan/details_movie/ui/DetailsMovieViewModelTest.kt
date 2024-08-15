package com.johan.details_movie.ui

import app.cash.turbine.test
import com.johan.details_movie.model.MovieDetail
import com.johan.details_movie.model.MovieDetailState
import com.johan.details_movie.model.MovieDetailsRepository
import com.johan.network.model.MovieDetailResponse
import com.johan.test_utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsMovieViewModelTest {
    private val repository: MovieDetailsRepository = mock()
    private lateinit var viewModel: MovieDetailsViewModel

    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        viewModel = MovieDetailsViewModel(repository, testCoroutineRule.testDispatcher)
    }

    @Test
    fun initialStateIsLoading() = runTest {
        advanceUntilIdle()
        val shouldBeLoading = viewModel.detail.first()

        assert(shouldBeLoading is MovieDetailState.Loading)
    }

    @Test
    fun nullMovieIdShouldBeErrorState() = runTest {
        advanceUntilIdle()

        viewModel.detail.test {
            viewModel.getMovieDetail(null)
            val resultState = expectMostRecentItem()
            assert(resultState is MovieDetailState.Error)
        }
    }

    @Test
    fun withMovieIdButNullBodyResponseShouldBeEmptyState() = runTest {
        advanceUntilIdle()
        val movieId = "12345-abc"
        viewModel.detail.test {
            viewModel.getMovieDetail(movieId)
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is MovieDetailState.Empty)
        }
    }

    @Test
    fun withMovieIdShouldBeLoadedState() = runTest {
        advanceUntilIdle()
        val movieId = "12345-abc"
        val title = "title"
        whenever(repository.getMovieDetail(movieId)).thenReturn(
            MovieDetailResponse(
                title = title
            )
        )
        val movieDetail = MovieDetail(
            title = title
        )

        val loadedMovieDetail = MovieDetailState.Loaded(movieDetail = movieDetail)

        viewModel.detail.test {
            viewModel.getMovieDetail(movieId)
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is MovieDetailState.Loaded)
            assert(
                (shouldBeEmpty as MovieDetailState.Loaded).movieDetail!!.title.equals(
                    loadedMovieDetail.movieDetail?.title
                )
            )
        }
    }
}