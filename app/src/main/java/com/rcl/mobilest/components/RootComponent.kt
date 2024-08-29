package com.rcl.mobilest.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.rcl.mobilest.components.RootComponent.TopLevelConfiguration.HomeScreenConfiguration
import com.rcl.mobilest.components.RootComponent.TopLevelConfiguration.SettingsScreenConfiguration
import com.rcl.mobilest.components.home.HomeComponent
import com.rcl.mobilest.components.settings.SettingsComponent
import kotlinx.serialization.Serializable

class RootComponent(componentContext: ComponentContext) : ComponentContext by componentContext {
    private val stack = StackNavigation<TopLevelConfiguration>()

    fun navigateTo(config: TopLevelConfiguration) {
        stack.pushToFront(config)
    }

    val childStack = childStack(
        source = stack,
        serializer = TopLevelConfiguration.serializer(),
        initialConfiguration = HomeScreenConfiguration,
        childFactory = ::createChild,
        handleBackButton = true
    )

    private fun createChild(
        config: TopLevelConfiguration,
        context: ComponentContext,
    ): TopLevelChild {
        return when (config) {
            is SettingsScreenConfiguration -> TopLevelChild.Settings(
                SettingsComponent(
                    componentContext = context
                )
            )

            is HomeScreenConfiguration -> TopLevelChild.Home(
                HomeComponent(
                    componentContext = context
                )
            )
        }
    }

    sealed class TopLevelChild(open val component: ComponentContext) {
        data class Home(override val component: HomeComponent) : TopLevelChild(component)
        data class Settings(override val component: SettingsComponent) : TopLevelChild(component)
    }

    @Serializable
    sealed interface TopLevelConfiguration {
        @Serializable
        data object HomeScreenConfiguration : TopLevelConfiguration

        @Serializable
        data object SettingsScreenConfiguration : TopLevelConfiguration
    }
}