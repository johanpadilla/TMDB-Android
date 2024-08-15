package com.johan.details_movie.model

import com.johan.network.model.MovieDetailResponse
import com.johan.network.services.MovieService
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(private val movieService: MovieService) :
    MovieDetailsRepository {

    override suspend fun getMovieDetail(movieId: String): MovieDetailResponse {
        return movieService.getMovieDetail(movieId)
    }
}