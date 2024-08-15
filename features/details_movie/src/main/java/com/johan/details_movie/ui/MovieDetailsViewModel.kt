package com.johan.details_movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johan.details_movie.model.MovieDetailState
import com.johan.details_movie.model.MovieDetailsRepository
import com.johan.details_movie.model.toMovieDetail
import com.johan.network.NetworkResponse
import com.johan.network.model.MovieDetailResponse
import com.johan.network.utils.performApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieDetailsRepository,
    private val ioDispatcher: CoroutineContext
) : ViewModel() {
    private val _details = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val detail: StateFlow<MovieDetailState> = _details.asStateFlow()

    fun getMovieDetail(movieId: String?) {
        if (movieId != null && movieId.isEmpty().not()) {
            viewModelScope.launch(ioDispatcher) {
                val response = performApiCall(ioDispatcher) { repository.getMovieDetail(movieId) }
                _details.value = getStateFromResponse(response)
            }
        } else _details.value = MovieDetailState.Error
    }

    fun onRefresh(movieId: String?) {
        _details.value = MovieDetailState.Loading
        getMovieDetail(movieId)
    }

    private fun getStateFromResponse(movieDetailResponse: NetworkResponse<MovieDetailResponse>): MovieDetailState {
        return when (movieDetailResponse) {
            is NetworkResponse.Success -> {
                val response = movieDetailResponse.body
                if (response != null && response.title.isNullOrEmpty().not()) {
                    MovieDetailState.Loaded(
                        movieDetail = response.toMovieDetail()
                    )
                } else MovieDetailState.Empty

            }

            else -> MovieDetailState.Error
        }
    }

}