package com.films.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.films.theme.FilmsTheme
import com.films.theme.deepPink
import com.films.theme.green
import com.films.theme.red
import com.films.theme.transparent
import com.films.theme.unspecified
import com.films.ui.R
import org.koin.compose.KoinContext
import org.koin.dsl.koinApplication

enum class FilmCategory(
    val nameRes: Int,
    val category: String
) {
    POPULAR(R.string.film_category_popular, "popular"),
    NOW_PLAYING(R.string.film_category_now_playing, "now_playing"),
    TOP_RATED(R.string.film_category_top, "top_rated"),
    UPCOMING(R.string.film_category_upcoming, "upcoming")
}

enum class AppSuccess(
    val iconRes: Int,
    val messageRes: Int,
    val iconTint: Color = unspecified
) {
    ADDED_TO_FAVORITES(R.drawable.favorite_fill, R.string.added_to_favorites, deepPink),
    REMOVED_FROM_FAVORITES(R.drawable.favorite, R.string.removed_from_favorites, deepPink),
    ADDED_TO_WATCH_LATER(R.drawable.time_add, R.string.added_to_watch_later, green),
    REMOVED_FROM_WATCH_LATER(R.drawable.time_delete, R.string.removed_from_watch_later, red)
}

const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"

fun Modifier.topBarPartialBorder(
    strokeWidth: Dp,
    color: Color,
    cornerRadius: Dp,
    extensionLength: Dp = 20.dp
) = this.drawWithContent {
    drawContent()

    val stroke = strokeWidth.toPx()
    val radius = cornerRadius.toPx()
    val extension = extensionLength.toPx()
    val width = size.width
    val height = size.height

    val path = Path().apply {
        moveTo(stroke / 2, height - radius - extension)
        lineTo(stroke / 2, height - radius)
        arcTo(
            rect = Rect(
                left = stroke / 2, top = height - radius * 2 + stroke / 2,
                right = radius * 2 - stroke / 2, bottom = height - stroke / 2
            ),
            startAngleDegrees = 180f, sweepAngleDegrees = -90f, forceMoveTo = false
        )
        arcTo(
            rect = Rect(
                left = width - radius * 2 + stroke / 2, top = height - radius * 2 + stroke / 2,
                right = width - stroke / 2, bottom = height - stroke / 2
            ),
            startAngleDegrees = 90f, sweepAngleDegrees = -90f, forceMoveTo = false
        )
        lineTo(width - stroke / 2, height - radius - extension)
    }

    val fadeBrush = Brush.verticalGradient(
        colors = listOf(transparent, color),
        startY = height - radius - extension,
        endY = height - radius
    )

    drawPath(path = path, brush = fadeBrush, style = Stroke(width = stroke, cap = StrokeCap.Round))
}

fun Modifier.bottomBarPartialBorder(
    strokeWidth: Dp,
    color: Color,
    cornerRadius: Dp,
    extensionLength: Dp = 20.dp
) = this.drawWithContent {
    drawContent()

    val stroke = strokeWidth.toPx()
    val radius = cornerRadius.toPx()
    val extension = extensionLength.toPx()
    val width = size.width

    val path = Path().apply {
        moveTo(stroke / 2, radius + extension)
        lineTo(stroke / 2, radius)
        arcTo(
            rect = Rect(
                left = stroke / 2, top = stroke / 2,
                right = radius * 2 - stroke / 2, bottom = radius * 2 - stroke / 2
            ),
            startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        arcTo(
            rect = Rect(
                left = width - radius * 2 + stroke / 2, top = stroke / 2,
                right = width - stroke / 2, bottom = radius * 2 - stroke / 2
            ),
            startAngleDegrees = 270f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        lineTo(width - stroke / 2, radius + extension)
    }

    val fadeBrush = Brush.verticalGradient(
        colors = listOf(color, transparent),
        startY = radius,
        endY = radius + extension
    )

    drawPath(path = path, brush = fadeBrush, style = Stroke(width = stroke, cap = StrokeCap.Round))
}

@Composable
@Preview(
    name = "Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun Preview() {
    val koin = remember {
        koinApplication {
            modules(previewModule)
        }.koin
    }
    KoinContext(context = koin) {
        FilmsTheme(onThemeChange = {}) {

        }
    }
}
