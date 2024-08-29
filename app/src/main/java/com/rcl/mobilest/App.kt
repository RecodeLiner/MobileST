package com.rcl.mobilest;

import android.app.Application
import org.koin.core.context.startKoin
import com.rcl.mobilest.di.theme.ThemeModel.themeModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //Koin - kotlin DI
        startKoin {
            modules(
                themeModule
            )
        }
    }
}
