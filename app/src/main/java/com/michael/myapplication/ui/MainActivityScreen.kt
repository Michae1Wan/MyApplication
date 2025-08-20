package com.michael.myapplication.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.michael.myapplication.R
import com.michael.myapplication.di.appModule
import com.michael.myapplication.ui.restaurant.RestaurantListScreen
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.mp.KoinPlatformTools

enum class AppDestinations (
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    PROFILE(R.string.navbar_title_profile, Icons.Default.AccountCircle, R.string.navbar_title_profile),
    RESTAURANTS(R.string.navbar_title_restaurants, Icons.Default.Home, R.string.navbar_title_restaurants),
    SETTINGS(R.string.navbar_title_settings, Icons.Default.Settings, R.string.navbar_title_settings),
}

@Composable
fun MainActivityScreen() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.RESTAURANTS) }

    NavigationSuiteScaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.displayCutout),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        // Destination content
        when (currentDestination) {
            AppDestinations.PROFILE -> ProfileScreen()
            AppDestinations.RESTAURANTS -> RestaurantListScreen()
            AppDestinations.SETTINGS -> SettingsScreen()
        }
    }
}

@PreviewScreenSizes
@Composable
fun MainActivityScreenPreview() {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        KoinApplication(application = {
            modules(appModule)
        }) {
            MaterialTheme { MainActivityScreen() }
        }
    } else {
        MaterialTheme { MainActivityScreen() }
    }
}