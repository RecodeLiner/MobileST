package com.rcl.mobilest.components.settings

import androidx.annotation.StringRes
import com.arkivanov.decompose.ComponentContext
import com.rcl.mobilest.R
import com.rcl.mobilest.di.config.ConfigManager
import com.rcl.mobilest.di.theme.ThemeManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsComponent(componentContext: ComponentContext) : ComponentContext by componentContext,
    KoinComponent {
    val configManager: ConfigManager by inject()
    val themeManager: ThemeManager by inject()

    fun updateState(isDownload: Boolean, newValue: Boolean) {
        configManager.setToggle(isDown = isDownload, newValue = newValue)
    }

    enum class DarkThemeEnum(val value: Boolean?, @StringRes val id: Int) {
        Dark(true, R.string.dark_theme),
        Light(false, R.string.light_theme),
        System(null, R.string.default_theme)
    }
}