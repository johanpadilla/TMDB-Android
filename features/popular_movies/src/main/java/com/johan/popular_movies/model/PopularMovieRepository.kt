package com.johan.popular_movies.model

import com.johan.network.NetworkResponse
import com.johan.network.model.PopularMovieResponse

interface PopularMovieRepository {

    suspend fun getPopularMovies(): NetworkResponse<PopularMovieResponse, Throwable>
}