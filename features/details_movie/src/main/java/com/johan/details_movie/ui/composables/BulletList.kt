package com.johan.details_movie.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.johan.details_movie.R

@Composable
fun BulletList(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = stringResource(id = R.string.bullet))
        Text(text = text)
    }
}