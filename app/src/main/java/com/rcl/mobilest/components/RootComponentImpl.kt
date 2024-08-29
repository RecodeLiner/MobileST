package com.rcl.mobilest.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.mobilest.R
import com.rcl.mobilest.R.string.bottom_settings
import com.rcl.mobilest.components.RootComponent.TopLevelConfiguration.HomeScreenConfiguration
import com.rcl.mobilest.components.RootComponent.TopLevelConfiguration.SettingsScreenConfiguration
import com.rcl.mobilest.components.home.HomeComponentImpl
import com.rcl.mobilest.components.settings.SettingsComponentImpl

val navItemList = listOf(
    NavItem(
        configuration = HomeScreenConfiguration,
        icon = Icons.Outlined.Home,
        textId = R.string.bottom_home,
    ),
    NavItem(
        configuration = SettingsScreenConfiguration,
        icon = Icons.Outlined.Settings,
        textId = bottom_settings,
    )
)

@Composable
fun RootComponentImpl(rootComponent: RootComponent) {
    //subscribe on stack to listen changes
    val stack by rootComponent.childStack.subscribeAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                navItemList.forEach { item ->
                    val selected = stack.items.last().configuration == item.configuration
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                rootComponent.navigateTo(item.configuration)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(id = item.textId)) }
                    )
                }
            }
        }
    ) { paddings ->
        //add paddings
        Box(Modifier.padding(paddings)) {
            //method from decompose to change current screen(can be replaced with when)
            Children(stack = stack) {
                when (val instance = it.instance) {
                    is RootComponent.TopLevelChild.Home ->
                        HomeComponentImpl(instance.component)

                    is RootComponent.TopLevelChild.Settings ->
                        SettingsComponentImpl(instance.component)
                }
            }
        }
    }
}