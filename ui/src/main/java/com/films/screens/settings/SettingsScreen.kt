package com.films.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.films.AppLanguage
import com.films.AppTheme
import com.films.components.BaseCard
import com.films.components.BaseIcon
import com.films.components.BaseText
import com.films.components.getIconRes
import com.films.components.getTitleRes
import com.films.components.previewModule
import com.films.components.topBarPartialBorder
import com.films.theme.BaseTheme
import com.films.theme.FilmsTheme
import com.films.theme.LocalLanguageChangeHandler
import com.films.theme.LocalSetLanguage
import com.films.theme.LocalSetTheme
import com.films.theme.LocalThemeChangeHandler
import com.films.ui.R
import org.koin.compose.KoinContext
import org.koin.dsl.koinApplication

@Composable
fun SettingsScreen(paddingValues: PaddingValues) {

    val currentTheme = LocalSetTheme.current
    val onThemeChange = LocalThemeChangeHandler.current
    val currentLanguage = LocalSetLanguage.current
    val onLanguageChange = LocalLanguageChangeHandler.current
    val layoutDirection = LocalLayoutDirection.current

    Column(
        Modifier
            .fillMaxSize()
            .background(BaseTheme.colors.screenBack)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = paddingValues.calculateStartPadding(layoutDirection) + 12.dp,
                    end = paddingValues.calculateEndPadding(layoutDirection) + 12.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BaseCard(
                modifier = Modifier.weight(1f),
                backGrad = BaseTheme.colors.cardGrad
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    BaseText(
                        stringResource(R.string.theme),
                        textAlign = TextAlign.Center,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                    )
                    AppTheme.entries.forEach { theme ->
                        ThemeItem(
                            option = theme,
                            isSelected = currentTheme == theme,
                            onClick = { onThemeChange(theme) }
                        )
                    }
                }
            }

            BaseCard(
                modifier = Modifier.weight(1f),
                backGrad = BaseTheme.colors.cardGrad
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BaseText(
                        stringResource(R.string.language),
                        textAlign = TextAlign.Center,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                    AppLanguage.entries.forEach { language ->
                        LanguageItem(
                            option = language,
                            isSelected = currentLanguage == language,
                            onClick = { onLanguageChange(language) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeItem(
    option: AppTheme,
    isSelected: Boolean,
    onClick: (AppTheme) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BaseText(
            stringResource(option.getTitleRes()),
            maxLines = 1,
            textStyle = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier
                .topBarPartialBorder(
                    strokeWidth = 1.5.dp,
                    color = BaseTheme.colors.borderPanel,
                    cornerRadius = 14.dp
                )
                .padding(bottom = 6.dp)
                .fillMaxWidth()
                .clickable(onClick = { onClick(option) }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BaseIcon(
                option.getIconRes(),
                iconTint = BaseTheme.colors.iconTint,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )

            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = BaseTheme.colors.iconTint,
                    unselectedColor = BaseTheme.colors.iconTint
                )
            )
        }
    }
}

@Composable
private fun LanguageItem(
    option: AppLanguage,
    isSelected: Boolean,
    onClick: (AppLanguage) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = { onClick(option) })
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BaseText(
            stringResource(option.getTitleRes()),
            maxLines = 1,
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = BaseTheme.colors.iconTint,
                unselectedColor = BaseTheme.colors.iconTint
            )
        )
    }
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
            SettingsScreen(paddingValues = PaddingValues())
        }
    }
}