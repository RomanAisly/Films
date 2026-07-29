package com.films.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.films.components.AdaptiveFilmListDetailPane
import com.films.components.LayoutMode
import com.films.screens.favorites.FavoritesScreen
import com.films.screens.home.HomeScreen
import com.films.screens.settings.SettingsScreen
import com.films.screens.watch_later.WatchLaterScreen
import com.films.theme.BaseTheme
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun BottomNavGraph(
    layoutMode: LayoutMode,
    restoredFilmId: Int?,
    onRestored: () -> Unit,
    onNavigateToDetails: (Int) -> Unit
) {
    val config = remember {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclassesOfSealed<Routes>()
                }
            }
        }
    }

    val bottomBackStack = rememberNavBackStack(config, Routes.Home)
    val currentTab = bottomBackStack.last()
    val isLandscapeLayout = layoutMode != LayoutMode.PORTRAIT

    val slideDuration = 500
    val fadeDuration = 500

    BackHandler(enabled = currentTab != Routes.Home) {
        bottomBackStack.clear()
        bottomBackStack.add(Routes.Home)
    }

    Scaffold(
        containerColor = BaseTheme.colors.screenBack,
        bottomBar = {
            if (!isLandscapeLayout) {
                BottomNavBar(
                    currentTab = currentTab,
                    layoutMode = layoutMode,
                    onTabSelected = { tabRoute ->
                        if (currentTab != tabRoute) {
                            bottomBackStack.clear()
                            bottomBackStack.add(tabRoute)
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (isLandscapeLayout) {
                BottomNavBar(
                    currentTab = currentTab,
                    layoutMode = layoutMode,
                    onTabSelected = { tabRoute ->
                        if (currentTab != tabRoute) {
                            bottomBackStack.clear()
                            bottomBackStack.add(tabRoute)
                        }
                    }
                )
            }
            NavDisplay(
                backStack = bottomBackStack,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                transitionSpec = {
                    (slideInVertically(
                        initialOffsetY = { it / 3 },
                        animationSpec = tween(slideDuration)
                    ) + fadeIn(animationSpec = tween(fadeDuration))) togetherWith
                            (slideOutVertically(
                                targetOffsetY = { -it / 3 },
                                animationSpec = tween(slideDuration)
                            ) + fadeOut(animationSpec = tween(fadeDuration)))
                },
                popTransitionSpec = {
                    (slideInVertically(
                        initialOffsetY = { -it / 3 },
                        animationSpec = tween(slideDuration)
                    ) + fadeIn(animationSpec = tween(fadeDuration))) togetherWith
                            (slideOutVertically(
                                targetOffsetY = { it / 3 },
                                animationSpec = tween(slideDuration)
                            ) + fadeOut(animationSpec = tween(fadeDuration)))
                },
                entryProvider = entryProvider {

                    entry<Routes.Home> {
                        AdaptiveFilmListDetailPane(
                            layoutMode = layoutMode,
                            restoredFilmId = restoredFilmId,
                            onRestored = onRestored, onNavigateToRootDetails = onNavigateToDetails
                        ) { isDetailOpen, onFilmClick ->
                            HomeScreen(
                                layoutMode = layoutMode,
                                isDetailOpen = isDetailOpen,
                                paddingValues = paddingValues,
                                onFilmClick = onFilmClick
                            )
                        }
                    }

                    entry<Routes.Favorites> {
                        AdaptiveFilmListDetailPane(
                            layoutMode = layoutMode,
                            restoredFilmId = restoredFilmId,
                            onRestored = onRestored, onNavigateToRootDetails = onNavigateToDetails
                        ) { isDetailOpen, onFilmClick ->
                            FavoritesScreen(
                                layoutMode = layoutMode,
                                isDetailOpen = isDetailOpen,
                                paddingValues = paddingValues,
                                onFilmClick = onFilmClick
                            )
                        }
                    }

                    entry<Routes.WatchLater> {
                        AdaptiveFilmListDetailPane(
                            layoutMode = layoutMode,
                            restoredFilmId = restoredFilmId,
                            onRestored = onRestored, onNavigateToRootDetails = onNavigateToDetails
                        ) { isDetailOpen, onFilmClick ->
                            WatchLaterScreen(
                                layoutMode = layoutMode,
                                isDetailOpen = isDetailOpen,
                                paddingValues = paddingValues,
                                onFilmClick = onFilmClick
                            )
                        }
                    }

                    entry<Routes.Settings> {
                        SettingsScreen(paddingValues = paddingValues)
                    }
                })
        }
    }
}
