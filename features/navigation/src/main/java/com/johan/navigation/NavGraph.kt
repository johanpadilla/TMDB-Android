package com.johan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.johan.details_movie.ui.MovieDetailScreen
import com.johan.popular_movies.ui.PopularMovieScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Destinations.PopularMovie.route
    ) {
        composable(Destinations.PopularMovie.route) {
            PopularMovieScreen(onPopularMovieClicked = {
                navController.navigate("movie_detail/$it")
            })
        }
        composable(Destinations.MovieDetail.route) {
            val movieId = it.arguments?.getString("id")
                ?.let(::requireNotNull)
                .orEmpty()
            MovieDetailScreen(movieId)
        }
    }
}