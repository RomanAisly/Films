package com.films.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.films.components.BaseIcon
import com.films.components.LayoutMode
import com.films.components.bottomBarPartialBorder
import com.films.theme.BaseTheme
import com.films.theme.lightBlue
import com.films.theme.transparent
import com.films.ui.R

data class BottomTabItem(
    val route: Routes,
    val icon: Int
)

@Composable
fun BottomNavBar(
    currentTab: Any,
    layoutMode: LayoutMode,
    onTabSelected: (Routes) -> Unit
) {
    val bottomScreens = remember {
        listOf(
            BottomTabItem(
                route = Routes.Home,
                icon = R.drawable.home
            ),
            BottomTabItem(
                route = Routes.Favorites,
                icon = R.drawable.favorite
            ),
            BottomTabItem(
                route = Routes.WatchLater,
                icon = R.drawable.time_quarter
            ),
            BottomTabItem(
                route = Routes.Settings,
                icon = R.drawable.settings
            )
        )
    }

    if (layoutMode != LayoutMode.PORTRAIT) {
        NavigationRail(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
                .background(BaseTheme.colors.navBarLand),
            containerColor = transparent,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            bottomScreens.forEach { item ->
                val isSelected = currentTab::class == item.route::class

                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    NavigationRailItem(
                        selected = isSelected,
                        onClick = { onTabSelected(item.route) },
                        icon = { TabIcon(item = item, isSelected = isSelected) },
                        colors = NavigationRailItemDefaults.colors(indicatorColor = transparent)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    } else {
        NavigationBar(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp
                    )
                )
                .background(BaseTheme.colors.navBarPort)
                .bottomBarPartialBorder(
                    strokeWidth = 1.5.dp,
                    color = BaseTheme.colors.borderPanel,
                    cornerRadius = 14.dp
                ),
            containerColor = transparent,
            tonalElevation = 0.dp,
        ) {
            bottomScreens.forEach { item ->
                val isSelected = currentTab::class == item.route::class

                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onTabSelected(item.route) },
                        icon = { TabIcon(item = item, isSelected = isSelected) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = transparent
                        )
                    )
                }
            }

        }
    }
}

@Composable
private fun TabIcon(item: BottomTabItem, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                color = if (isSelected) BaseTheme.colors.iconBackSecondary else BaseTheme.colors.buttPrimary,
                shape = CircleShape
            )
            .border(
                border = if (isSelected) BorderStroke(2.dp, lightBlue) else BorderStroke(
                    0.dp,
                    transparent
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        BaseIcon(
            iconId = item.icon,
            iconTint = if (isSelected) BaseTheme.colors.iconTint else BaseTheme.colors.iconBackTertiary,
        )
    }
}