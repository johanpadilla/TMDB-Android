package com.johan.details_movie.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.johan.details_movie.R
import com.johan.details_movie.model.ProductionCompany
import com.johan.shared.utils.toFullPosterURL

@Composable
fun ProductionCompany(companies: List<ProductionCompany>) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    Column {
        Text(text = "${stringResource(id = R.string.production_companies_text)}:")//, style = bodyBold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding))) {
            items(companies.size) { index ->
                if (companies[index].logoPath.isNullOrEmpty().not()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(companies[index].logoPath?.toFullPosterURL())
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.rowImageHeight))
                            //.background(color = LocalCustomColorsPalette.current.productionCompaniesBackgroundColor)
                            .padding(dimensionResource(id = R.dimen.smallHorizontalPadding))
                    )
                }
            }
        }
    }
}