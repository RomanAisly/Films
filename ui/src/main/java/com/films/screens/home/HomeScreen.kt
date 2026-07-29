package com.films.screens.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.films.components.FilmCategory
import com.films.components.FilmsItem
import com.films.components.LayoutMode
import com.films.components.LoadingScreen
import com.films.components.RefreshIndicator
import com.films.components.SnackBarFlow
import com.films.components.TabButton
import com.films.components.getIconRes
import com.films.components.getMessageRes
import com.films.components.toLocalizedGenresString
import com.films.components.topBarPartialBorder
import com.films.theme.BaseTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    layoutMode: LayoutMode,
    viewModel: HomeViewModel = koinViewModel(),
    paddingValues: PaddingValues,
    onFilmClick: (id: Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val refreshState = rememberPullToRefreshState()
    val gridState = rememberLazyGridState()
    val isScrollInProgress = gridState.isScrollInProgress
    val layoutDirection = LocalLayoutDirection.current

    val columnsCount = when (layoutMode) {
        LayoutMode.PORTRAIT -> 2
        LayoutMode.LANDSCAPE_PHONE -> 4
        LayoutMode.FOLD_TABLET -> 5
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseTheme.colors.screenBack)
    ) {
        RefreshIndicator(
            state = refreshState,
            isRefreshing = state.isRefreshing,
            paddingValues = paddingValues,
            onRefresh = { viewModel.refreshFilms() }
        ) {
            if (state.films.isEmpty() && !state.isRefreshing) {
                LoadingScreen()
            } else {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(columnsCount),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        start = 8.dp,
                        end = 8.dp + paddingValues.calculateEndPadding(layoutDirection),
                        top = paddingValues.calculateTopPadding() + 72.dp,
                        bottom = 8.dp + paddingValues.calculateBottomPadding()
                    )
                ) {
                    items(state.films, key = { it.id }) { films ->
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
            }
        }
        Row(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = 14.dp,
                        bottomEnd = 14.dp
                    )
                )
                .background(BaseTheme.colors.topBarPanel)
                .topBarPartialBorder(
                    strokeWidth = 1.5.dp,
                    color = BaseTheme.colors.borderPanel,
                    cornerRadius = 14.dp
                )
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxWidth()
                .padding(6.dp)
                .horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FilmCategory.entries.forEach { category ->
                TabButton(
                    tabName = stringResource(category.nameRes),
                    tabColor = if (state.selectedCategory == category.category) BaseTheme.colors.buttTertiary else BaseTheme.colors.buttSecondary,
                    border = if (state.selectedCategory == category.category) BorderStroke(
                        2.dp,
                        BaseTheme.colors.border
                    ) else null,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = {
                        viewModel.changeCategory(category)
                    }
                )
            }
        }
        SnackBarFlow(
            snackBarFlow = viewModel.snack,
            iconRes = { it.getIconRes() },
            messageRes = { it.getMessageRes() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingValues.calculateBottomPadding())
        )
    }
}