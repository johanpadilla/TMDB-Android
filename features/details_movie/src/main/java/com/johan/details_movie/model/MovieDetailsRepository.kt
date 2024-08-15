package com.johan.details_movie.model

import com.johan.network.model.MovieDetailResponse

interface MovieDetailsRepository {
    suspend fun getMovieDetail(movieId: String): MovieDetailResponse
}