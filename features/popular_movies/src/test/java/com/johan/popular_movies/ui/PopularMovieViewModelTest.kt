package com.johan.popular_movies.ui

import app.cash.turbine.test
import com.johan.network.model.PopularMovieResponse
import com.johan.network.model.Results
import com.johan.popular_movies.TestCoroutineRule
import com.johan.popular_movies.model.PopularMovieRepository
import com.johan.popular_movies.model.PopularMovieState
import com.johan.popular_movies.model.toPopularMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PopularMovieViewModelTest {

    private lateinit var viewModel: PopularMovieViewModel
    private val repository: PopularMovieRepository = mock()

    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        viewModel = PopularMovieViewModel(repository, testCoroutineRule.testDispatcher)
    }

    @Test
    fun initialStateIsLoading() = runTest {
        advanceUntilIdle()
        val shouldBeLoading = viewModel.movies.first()

        assert(shouldBeLoading is PopularMovieState.Loading)
    }

    @Test
    fun loadedPopularMovieShouldBeLoadedState() = runTest {
        advanceUntilIdle()
        val page = 1
        val popularMovieResponse = PopularMovieResponse(
            results = listOf(Results()), page = 0, totalPages = 0
        )
        val params = mutableMapOf(("language" to "en-US"))
        params.putAll(mapOf(("page" to page.toString())))
        whenever(repository.getPopularMovies(params)).thenReturn(
            popularMovieResponse
        )

        val loadedPopular = PopularMovieState.Loaded(
            totalPages = popularMovieResponse.totalPages,
            currentPage = popularMovieResponse.page,
            movies = popularMovieResponse.results.map { it.toPopularMovie() })

        viewModel.movies.test {
            //viewModel.getMovies(page)
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeLoaded = awaitItem()
            assert(shouldBeLoaded is PopularMovieState.Loaded)
            assert((shouldBeLoaded as PopularMovieState.Loaded).currentPage == loadedPopular.currentPage)
        }
    }

    @Test
    fun noResultsShouldBeEmptyState() = runTest {
        advanceUntilIdle()
        val page = anyInt()
        val params = mutableMapOf(("language" to "en-US"))
        params.putAll(mapOf(("page" to page.toString())))

        viewModel.movies.test {
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is PopularMovieState.Empty)
        }
    }

    @Test
    fun withRestartNoResultsShouldBeEmptyState() = runTest {
        advanceUntilIdle()
        val page = anyInt()
        val params = mutableMapOf(("language" to "en-US"))
        params.putAll(mapOf(("page" to page.toString())))

        viewModel.movies.test {
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is PopularMovieState.Empty)

            viewModel.restart()
            val shouldBeLoading2 = awaitItem()
            assert(shouldBeLoading2 is PopularMovieState.Loading)
            val shouldBeEmpty2 = awaitItem()
            assert(shouldBeEmpty2 is PopularMovieState.Empty)
        }
    }
}