package com.rcl.mobilest.components.settings

import com.arkivanov.decompose.ComponentContext
import com.rcl.mobilest.di.config.ConfigManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsComponent(componentContext: ComponentContext) : ComponentContext by componentContext,
    KoinComponent {
    val configManager: ConfigManager by inject()

    fun updateState(isDownload: Boolean, newValue: Boolean) {
        configManager.setToggle(isDown = isDownload, newValue = newValue)
    }
}