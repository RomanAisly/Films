package com.films.components

import com.films.screens.details.DetailsViewModel
import com.films.screens.favorites.FavoritesViewModel
import com.films.screens.home.HomeViewModel
import com.films.screens.settings.SettingsViewModel
import com.films.screens.watch_later.WatchLaterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val previewModule = module {

}
val viewModelsModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::WatchLaterViewModel)
    viewModelOf(::SettingsViewModel)
}