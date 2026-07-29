package com.films.screens.favorites

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.films.components.BaseText
import com.films.components.FilmsItem
import com.films.components.LayoutMode
import com.films.components.toLocalizedGenresString
import com.films.components.topBarPartialBorder
import com.films.theme.BaseTheme
import com.films.theme.deepPink
import com.films.ui.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    layoutMode: LayoutMode,
    isDetailOpen: Boolean,
    viewModel: FavoritesViewModel = koinViewModel(),
    paddingValues: PaddingValues,
    onFilmClick: (id: Int) -> Unit
) {
    val favoriteFilms by viewModel.favoriteFilms.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    val isScrollInProgress = gridState.isScrollInProgress
    val layoutDirection = LocalLayoutDirection.current

    val columnsCount = when {
        layoutMode == LayoutMode.PORTRAIT -> 2
        isDetailOpen -> 2
        layoutMode == LayoutMode.LANDSCAPE_PHONE -> 4
        layoutMode == LayoutMode.FOLD_TABLET -> 4
        else -> 2
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseTheme.colors.screenBack)
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(columnsCount),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp + paddingValues.calculateEndPadding(layoutDirection),
                top = paddingValues.calculateTopPadding() + 58.dp,
                bottom = 8.dp + paddingValues.calculateBottomPadding()
            )
        ) {
            items(favoriteFilms, key = { it.id }) { films ->
                FilmsItem(
                    modifier = Modifier.animateItem(
                        placementSpec = if (isScrollInProgress) {
                            null
                        } else {
                            spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        }
                    ),
                    poster = films.poster_path,
                    title = films.title,
                    genres = films.genreIds.toLocalizedGenresString(),
                    releaseDate = films.release_date,
                    rating = films.vote_average,
                    onFilmClick = { onFilmClick(films.id) })
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp))
                .background(BaseTheme.colors.topBarPanel)
                .topBarPartialBorder(
                    strokeWidth = 1.5.dp,
                    color = BaseTheme.colors.borderPanel,
                    cornerRadius = 14.dp
                )
                .padding(top = paddingValues.calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            BaseText(
                text = stringResource(R.string.favorites),
                textStyle = MaterialTheme.typography.headlineLarge,
                textColor = deepPink,
                modifier = Modifier.padding(bottom = 14.dp, top = 8.dp)
            )
        }
    }
}