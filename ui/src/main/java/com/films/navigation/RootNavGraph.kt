package com.films.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.films.components.LayoutMode
import com.films.screens.details.DetailsScreen
import com.films.screens.details.DetailsViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalSerializationApi::class)
@Composable
fun RootNavGraph() {
    val config = remember {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclassesOfSealed<Routes>()
                }
            }
        }
    }
    val rootBackStack = rememberNavBackStack(config, Routes.BottomNavGraph)

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(600)
    val isCompactHeight = !windowSizeClass.isHeightAtLeastBreakpoint(480)

    val layoutMode = when {
        isCompactHeight -> LayoutMode.LANDSCAPE_PHONE
        isWideScreen -> LayoutMode.FOLD_TABLET
        else -> LayoutMode.PORTRAIT
    }
    val isLandscapeLayout = layoutMode != LayoutMode.PORTRAIT

    var restoredFilmId by rememberSaveable { mutableStateOf<Int?>(null) }

    val fadeDuration = 900

    LaunchedEffect(isLandscapeLayout) {
        if (isLandscapeLayout) {
            val currentRoute = rootBackStack.last()
            if (currentRoute is Routes.Details) {
                restoredFilmId = currentRoute.id
                rootBackStack.removeAt(rootBackStack.lastIndex)
            }
        }
    }
    NavDisplay(
        backStack = rootBackStack,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(animationSpec = tween(fadeDuration)) togetherWith fadeOut(
                animationSpec = tween(
                    fadeDuration
                )
            )
        },
        popTransitionSpec = {
            fadeIn(animationSpec = tween(fadeDuration)) togetherWith fadeOut(
                animationSpec = tween(
                    fadeDuration
                )
            )
        },
        entryProvider = entryProvider {

            entry<Routes.BottomNavGraph> {
                BottomNavGraph(
                    layoutMode = layoutMode,
                    restoredFilmId = restoredFilmId,
                    onRestored = { restoredFilmId = null },
                    onNavigateToDetails = { filmId ->
                        rootBackStack.add(Routes.Details(filmId))
                    }
                )
            }

            entry<Routes.Details> { route ->
                val viewModel: DetailsViewModel = koinViewModel(
                    key = route.id.toString(),
                    parameters = { parametersOf(route.id) })

                DetailsScreen(
                    viewModel = viewModel,
                    onBack = {
                        if (rootBackStack.size > 1) {
                            rootBackStack.removeAt(rootBackStack.lastIndex)
                        }
                    }
                )
            }
        }
    )
}