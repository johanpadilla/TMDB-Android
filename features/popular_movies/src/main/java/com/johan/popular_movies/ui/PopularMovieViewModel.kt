package com.johan.popular_movies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johan.network.NetworkResponse
import com.johan.network.di.IoDispatcher
import com.johan.network.model.PopularMovieResponse
import com.johan.network.utils.performApiCall
import com.johan.popular_movies.flow.makeRestartable
import com.johan.popular_movies.model.PopularMovieRepository
import com.johan.popular_movies.model.PopularMovieState
import com.johan.popular_movies.model.toPopularMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val popularMovieRepository: PopularMovieRepository,
    @IoDispatcher private val ioDispatcher: CoroutineContext
) : ViewModel() {

    private val restarter = SharingStarted.WhileSubscribed(FIVE_SECONDS).makeRestartable()

    val movies: StateFlow<PopularMovieState> by lazy {
        flow {
            emit(getStateFromResponse(performApiCall(ioDispatcher) {
                popularMovieRepository.getPopularMovies(
                    mapOf(
                        "language" to "en-US",
                        "page" to "1"
                    )
                )
            }))
        }.catch {
            emit(PopularMovieState.Empty)
        }
            .flowOn(ioDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = restarter,
                initialValue = PopularMovieState.Loading
            )
    }


    private fun getStateFromResponse(moviesResponse: NetworkResponse<PopularMovieResponse>): PopularMovieState {
        return when (moviesResponse) {
            is NetworkResponse.Success -> {
                val body = moviesResponse.body
                if (body != null && body.results.isNotEmpty()) {
                    PopularMovieState.Loaded(
                        currentPage = moviesResponse.body.page,
                        totalPages = moviesResponse.body.totalPages,
                        movies = moviesResponse.body.results.map { it.toPopularMovie() })
                } else PopularMovieState.Empty
            }

            else -> PopularMovieState.Error
        }
    }

    fun restart() = restarter.restart()

    companion object {
        private const val FIVE_SECONDS: Long = 5_000
    }
}