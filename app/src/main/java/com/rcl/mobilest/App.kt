package com.rcl.mobilest

import android.app.Application
import com.rcl.mobilest.di.config.ConfigModel.confModule
import com.rcl.mobilest.di.connect.ConnectivityModule.connModule
import com.rcl.mobilest.di.theme.ThemeModel.themeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //Koin - kotlin DI
        startKoin {
            androidContext(this@App)
            modules(
                themeModule,
                confModule,
                connModule
            )
        }
    }
}
