package com.films.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.films.theme.BaseTheme
import com.films.ui.R

@Composable
fun FilmsItem(
    modifier: Modifier = Modifier,
    poster: Any?,
    title: String,
    genres: String,
    releaseDate: String,
    rating: Double,
    onFilmClick: () -> Unit
) {

    BaseCard(
        modifier = modifier.clickable { onFilmClick() },
        shape = MaterialTheme.shapes.large,
        containerColor = BaseTheme.colors.cardSecondary
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoilImage(
                model = IMAGE_URL + poster,
                placeholder = R.drawable.placeholder_image,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = MaterialTheme.shapes.large
                    )
                    .aspectRatio(2f / 3f)
            )
            BaseText(
                text = title,
                maxLines = 2,
                minLines = 2,
                textStyle = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            BaseText(
                text = genres,
                maxLines = 1,
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            BaseText(
                text = releaseDate,
                textStyle = MaterialTheme.typography.titleSmall,
            )
            RatingBar(rating = rating / 2, modifier = Modifier.padding(bottom = 6.dp))
        }
    }
}