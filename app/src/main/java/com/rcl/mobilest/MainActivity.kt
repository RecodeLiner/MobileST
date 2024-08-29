package com.rcl.mobilest

import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.retainedComponent
import com.rcl.mobilest.components.RootComponent
import com.rcl.mobilest.components.RootComponent.TopLevelConfiguration.SettingsScreenConfiguration
import com.rcl.mobilest.components.RootComponentImpl
import com.rcl.mobilest.di.theme.ThemeManager
import com.rcl.mobilest.ui.theme.MobileSTTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//KoinComponent - main part of injection in koin
class MainActivity : ComponentActivity(), KoinComponent {
    //ThemeManager designed specially for management from settings
    private val themeManager: ThemeManager by inject()
    //Root component - main navigator and container in app
    private lateinit var rootComponent: RootComponent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //"retained" make component full stable
        rootComponent = retainedComponent {
            RootComponent(it)
        }
        //Make app fullscreen with transparent navbar
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(TRANSPARENT, TRANSPARENT)
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)

        //handle intent, currently only work for enable "settings"
        //in android's app settings
        handleIntent(this.intent)


        setContent {
            MobileSTTheme(themeManager = themeManager) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        RootComponentImpl(rootComponent)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val action = intent.action

        when (action) {
            Intent.ACTION_APPLICATION_PREFERENCES -> {
                rootComponent.navigateTo(SettingsScreenConfiguration)
            }
            else -> {

            }
        }
    }
}