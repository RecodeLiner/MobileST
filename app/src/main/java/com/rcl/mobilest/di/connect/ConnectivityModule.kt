package com.rcl.mobilest.di.connect

import android.content.Context
import android.net.ConnectivityManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ConnectivityModule {
    val connModule = module {
        single {
            androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }
}