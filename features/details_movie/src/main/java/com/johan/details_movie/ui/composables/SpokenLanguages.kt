package com.johan.details_movie.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.johan.details_movie.R
import com.johan.details_movie.model.SpokenLanguage

@Composable
fun SpokenLanguages(spokenLanguages: List<SpokenLanguage>) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    Text(text = "${stringResource(id = R.string.available_language_text)}:")
    spokenLanguages.map {
        it.englishName?.let { language ->
            BulletList(text = language)
        }
    }
}