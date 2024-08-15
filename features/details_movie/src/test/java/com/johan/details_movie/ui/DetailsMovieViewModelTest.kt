package com.johan.details_movie.ui

import com.johan.details_movie.model.MovieDetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.kotlin.mock

class DetailsMovieViewModelTest {
    private val repository: MovieDetailsRepository = mock()
    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setup() {

    }

}