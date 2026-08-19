package com.films

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.films.navigation.RootNavGraph
import com.films.screens.settings.SettingsViewModel
import com.films.theme.AppLanguageProvider
import com.films.theme.FilmsTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val settingsViewModel: SettingsViewModel = koinViewModel()
            val currentTheme by settingsViewModel.themeState.collectAsStateWithLifecycle()
            val currentLanguage by settingsViewModel.languageState.collectAsStateWithLifecycle()
            val isDataLoaded = currentTheme != null && currentLanguage != null

            Crossfade(
                targetState = isDataLoaded,
                animationSpec = tween(durationMillis = 900)
            ) { isLoaded ->
                if (isLoaded) {
                    AppLanguageProvider(
                        setLanguage = currentLanguage!!,
                        onLanguageChange = { newLanguage ->
                            settingsViewModel.changeLanguage(newLanguage)
                        }) {
                        FilmsTheme(setTheme = currentTheme!!, onThemeChange = { newTheme ->
                            settingsViewModel.changeTheme(newTheme)
                        }) {
                            RootNavGraph()
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}