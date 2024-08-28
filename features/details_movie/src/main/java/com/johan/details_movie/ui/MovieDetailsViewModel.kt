package com.johan.details_movie.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johan.details_movie.model.MovieDetailState
import com.johan.details_movie.model.MovieDetailsRepository
import com.johan.details_movie.model.toMovieDetail
import com.johan.network.NetworkResponse
import com.johan.network.model.MovieDetailResponse
import com.johan.shared.navigation.Argument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieDetailsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _details = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val detail: StateFlow<MovieDetailState> = _details.asStateFlow()

    init {
        val movieId = savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)
        getMovieDetail(movieId)
    }

    fun getMovieDetail(movieId: String?) {
        if (movieId != null && movieId.isEmpty().not()) {
            viewModelScope.launch {
                val response = repository.getMovieDetail(movieId)
                _details.value = getStateFromResponse(response)
            }
        } else _details.value = MovieDetailState.Error
    }

    fun onRefresh(onError: Boolean = false) {
        if (onError) {
            _details.value = MovieDetailState.Loading
        } else {
            _details.value = (_details.value as MovieDetailState.Loaded).copy(isRefreshing = true)
        }
        getMovieDetail(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY))
    }

    private fun getStateFromResponse(movieDetailResponse: NetworkResponse<MovieDetailResponse, Throwable>): MovieDetailState {
        return when (movieDetailResponse) {
            is NetworkResponse.Success -> {
                val response = movieDetailResponse.body
                if (response.title.isNullOrEmpty().not()) {
                    MovieDetailState.Loaded(
                        movieDetail = response.toMovieDetail(), isRefreshing = false
                    )
                } else MovieDetailState.Empty

            }

            else -> MovieDetailState.Error
        }
    }
}