package com.johan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.johan.popular_movies.ui.PopularMovieScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Destinations.PopularMovie
    ) {
        composable<Destinations.PopularMovie> {
            PopularMovieScreen()
        }
    }
}