package com.johan.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.johan.details_movie.ui.MovieDetailScreen
import com.johan.popular_movies.ui.PopularMovieScreen
import com.johan.shared.navigation.Argument
import com.johan.shared.navigation.Screen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.POPULAR_MOVIE
    ) {
        composable(
            route = Screen.POPULAR_MOVIE
        ) {
            PopularMovieScreen {
                navController.navigate("${Screen.MOVIE_DETAILS}/$it")
            }
        }
        composable(
            route = "${Screen.MOVIE_DETAILS}/{${Argument.MOVIE_ID_KEY}}",
            arguments = listOf(
                navArgument(Argument.MOVIE_ID_KEY) {
                    NavType.StringType
                }
            )
        ) {
            MovieDetailScreen({ navController.navigateUp() })
        }
    }
}