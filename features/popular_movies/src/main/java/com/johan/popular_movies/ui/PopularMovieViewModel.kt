package com.johan.popular_movies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johan.network.NetworkResponse
import com.johan.network.model.PopularMovieResponse
import com.johan.popular_movies.model.PopularMovieRepository
import com.johan.popular_movies.model.PopularMovieState
import com.johan.popular_movies.model.toPopularMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val repository: PopularMovieRepository,
) : ViewModel() {
    private val _popularMovies = MutableStateFlow<PopularMovieState>(PopularMovieState.Loading)
    val popularMovies: StateFlow<PopularMovieState> = _popularMovies.asStateFlow()

    fun getPopularMovies() {
        viewModelScope.launch {
            val response = repository.getPopularMovies()
            _popularMovies.value = getStateFromResponse(response)
        }
    }

    fun refresh(onError: Boolean = false) {
        if(onError) {
            _popularMovies.value = PopularMovieState.Loading
        } else {
            _popularMovies.value =
                (_popularMovies.value as PopularMovieState.Loaded).copy(isRefreshing = true)
        }
        getPopularMovies()
    }

    private fun getStateFromResponse(moviesResponse: NetworkResponse<PopularMovieResponse, Throwable>): PopularMovieState {
        return when (moviesResponse) {
            is NetworkResponse.Success -> {
                val body = moviesResponse.body
                if (body.results.isNotEmpty()) {
                    PopularMovieState.Loaded(
                        currentPage = moviesResponse.body.page,
                        totalPages = moviesResponse.body.totalPages,
                        movies = moviesResponse.body.results.map { it.toPopularMovie() },
                        isRefreshing = false
                    )
                } else PopularMovieState.Empty
            }

            else -> PopularMovieState.Error
        }
    }
}