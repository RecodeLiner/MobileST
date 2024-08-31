package com.rcl.mobilest.di.config

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow

class ConfigManager(
    private val sharedPref: SharedPreferences,
) {
    val isDownstreamEnabled = MutableStateFlow(true)
    val isUpstreamEnabled = MutableStateFlow(true)

    init {
        isDownstreamEnabled.value = sharedPref.getBoolean("isDownstreamEnabled", true)
        isUpstreamEnabled.value = sharedPref.getBoolean("isUpstreamEnabled", true)
    }

    fun setToggle(isDown: Boolean, newValue: Boolean) {
        if (isDown) {
            with (sharedPref.edit()) {
                putBoolean("isDownstreamEnabled", newValue)
                apply()
            }
            isDownstreamEnabled.value = newValue
        }
        else {
            with (sharedPref.edit()) {
                putBoolean("isUpstreamEnabled", newValue)
                apply()
            }
            isUpstreamEnabled.value = newValue
        }
    }
}