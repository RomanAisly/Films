package com.films.local

import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.films.AppLanguage
import com.films.AppTheme
import com.films.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val dataStore: DataStore<Preferences>) : SettingsRepository {

    private inline fun <reified T : Enum<T>> getEnumFlow(
        key: Preferences.Key<String>,
        defaultValue: T
    ): Flow<T> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val name = prefs[key] ?: return@map defaultValue
            runCatching { enumValueOf<T>(name) }.getOrDefault(defaultValue)
        }

    override val themeFlow: Flow<AppTheme> = getEnumFlow(THEME_KEY, AppTheme.SYSTEM)
    override val languageFlow: Flow<AppLanguage> =
        getEnumFlow(LANGUAGE_KEY, getDefaultSystemLanguage())

    override suspend fun saveTheme(theme: AppTheme) {
        dataStore.edit { it[THEME_KEY] = theme.name }
    }

    override suspend fun saveLanguage(language: AppLanguage) {
        dataStore.edit { it[LANGUAGE_KEY] = language.name }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    private fun getDefaultSystemLanguage(): AppLanguage {
        val systemLocale = Resources.getSystem().configuration.locales[0]
        val systemLangCode = systemLocale.language

        return AppLanguage.entries.find { it.localeCode == systemLangCode }
            ?: AppLanguage.ENGLISH
    }
}
