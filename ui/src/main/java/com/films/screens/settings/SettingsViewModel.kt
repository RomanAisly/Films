package com.films.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.films.AppLanguage
import com.films.AppTheme
import com.films.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val themeState: StateFlow<AppTheme?> = settingsRepository.themeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val languageState: StateFlow<AppLanguage?> = settingsRepository.languageFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )


    fun changeTheme(newTheme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.saveTheme(newTheme)
        }
    }

    fun changeLanguage(newLanguage: AppLanguage) {
        viewModelScope.launch {
            settingsRepository.saveLanguage(newLanguage)
        }
    }
}