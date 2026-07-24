package com.films.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.films.AppTheme

@Immutable
data class BaseColors(
    val screenBack: Color,
    val card: Color,
    val cardSecondary: Color,
    val buttPrimary: Color,
    val buttSecondary: Color,
    val buttTertiary: Color,
    val iconBack: Color,
    val iconBackSecondary: Color,
    val iconBackTertiary: Color,
    val iconTint: Color,
    val text: Color,
    val border: Color,
    val borderPanel: Color,
    val panelGradientStart: Color,
    val panelGradientEnd: Color,
    val cardGradStart: Color,
    val cardGradEnd: Color,
) {
    val topBarPanel: Brush
        get() = Brush.verticalGradient(
            listOf(panelGradientStart, panelGradientEnd)
        )
    val bottBarPanel: Brush
        get() = Brush.verticalGradient(
            listOf(panelGradientEnd, panelGradientStart)
        )
    val cardGrad: Brush
        get() = Brush.horizontalGradient(
            listOf(cardGradStart, cardGradEnd)
        )
}

val lightColors = BaseColors(
    screenBack = mintCream,
    card = azure,
    cardSecondary = azure,
    buttPrimary = lightBlue,
    buttSecondary = archer,
    buttTertiary = white,
    iconBack = white,
    iconBackSecondary = white,
    iconBackTertiary = azure,
    iconTint = cornflowerBlue,
    text = black,
    border = lightSalmon,
    borderPanel = cornflowerBlue.copy(alpha = 0.3f),
    panelGradientStart = lightGray.copy(alpha = 0.8f),
    panelGradientEnd = skyBlue.copy(alpha = 0.8f),
    cardGradStart = azure,
    cardGradEnd = lightGray
)

val darkColors = BaseColors(
    screenBack = twilight,
    card = teal,
    cardSecondary = darkStateBlue.copy(alpha = 0.9f),
    buttPrimary = darkOliveGreen.copy(alpha = 0.9f),
    buttSecondary = teal.copy(alpha = 0.9f),
    buttTertiary = gray,
    iconBack = darkGray,
    iconBackSecondary = indigo.copy(alpha = 0.8f),
    iconBackTertiary = white,
    iconTint = white,
    text = white,
    border = yellow,
    borderPanel = white.copy(alpha = 0.3f),
    panelGradientStart = darkStateBlue.copy(alpha = 0.8f),
    panelGradientEnd = gray.copy(alpha = 0.8f),
    cardGradStart = darkGray,
    cardGradEnd = teal
)

object BaseTheme {
    val colors: BaseColors
        @Composable
        get() = LocalBaseColors.current
}

val LocalBaseColors = staticCompositionLocalOf<BaseColors> { error("No AppColors provided") }
val LocalSetTheme = staticCompositionLocalOf { AppTheme.SYSTEM }
val LocalThemeChangeHandler = staticCompositionLocalOf<(AppTheme) -> Unit> { {} }

@Composable
fun FilmsTheme(
    setTheme: AppTheme = AppTheme.SYSTEM,
    onThemeChange: (AppTheme) -> Unit,
    content: @Composable () -> Unit
) {
    val isDark = when (setTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) darkColors else lightColors
    val animatedColorScheme = animateColorSchemeAsState(colorScheme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }

    CompositionLocalProvider(
        LocalSetTheme provides setTheme,
        LocalThemeChangeHandler provides onThemeChange,
        LocalBaseColors provides animatedColorScheme
    ) {
        MaterialTheme(
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

@Composable
private fun animateColorSchemeAsState(targetColor: BaseColors): BaseColors {
    val animationSpec = tween<Color>(durationMillis = 400)
    return BaseColors(
        screenBack = animateColorAsState(targetColor.screenBack, animationSpec).value,
        card = animateColorAsState(targetColor.card, animationSpec).value,
        cardSecondary = animateColorAsState(targetColor.cardSecondary, animationSpec).value,
        buttPrimary = animateColorAsState(targetColor.buttPrimary, animationSpec).value,
        buttSecondary = animateColorAsState(targetColor.buttSecondary, animationSpec).value,
        buttTertiary = animateColorAsState(targetColor.buttTertiary, animationSpec).value,
        iconBack = animateColorAsState(targetColor.iconBack, animationSpec).value,
        iconBackSecondary = animateColorAsState(targetColor.iconBackSecondary, animationSpec).value,
        iconBackTertiary = animateColorAsState(targetColor.iconBackTertiary, animationSpec).value,
        iconTint = animateColorAsState(targetColor.iconTint, animationSpec).value,
        text = animateColorAsState(targetColor.text, animationSpec).value,
        border = animateColorAsState(targetColor.border, animationSpec).value,
        borderPanel = animateColorAsState(targetColor.borderPanel, animationSpec).value,
        panelGradientStart = animateColorAsState(
            targetColor.panelGradientStart,
            animationSpec
        ).value,
        panelGradientEnd = animateColorAsState(targetColor.panelGradientEnd, animationSpec).value,
        cardGradStart = animateColorAsState(targetColor.cardGradStart, animationSpec).value,
        cardGradEnd = animateColorAsState(targetColor.cardGradEnd, animationSpec).value
    )
}
