package com.johan.details_movie.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.johan.details_movie.R
import com.johan.details_movie.model.ProductionCountry

@Composable
fun ProductionCountry(countries: List<ProductionCountry>) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    Column {
        Text(text = "${stringResource(id = R.string.production_countries_text)}:")//, style = bodyBold)
        countries.map {
            it.name?.let { country ->
                BulletList(text = country)
            }
        }
    }
}