package com.rcl.mobilest.di.theme

import android.content.Context.MODE_PRIVATE
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ThemeModel {
    val themeModule = module {
        single {
            ThemeManager(
                sharedPref = androidContext().getSharedPreferences(
                    "app_preferences",
                    MODE_PRIVATE
                )
            )
        }
    }
}