package com.rcl.mobilest.di.config

import android.content.Context.MODE_PRIVATE
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ConfigModel {
    val confModule = module {
        single {
            ConfigManager(
                sharedPref = androidContext().getSharedPreferences(
                    "app_preferences",
                    MODE_PRIVATE
                )
            )
        }
    }
}