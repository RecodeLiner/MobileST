package com.rcl.mobilest.components.home

import android.net.ConnectivityManager
import com.arkivanov.decompose.ComponentContext
import com.rcl.mobilest.di.config.ConfigManager
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HomeComponent(componentContext: ComponentContext) : ComponentContext by componentContext,
    KoinComponent {
    val configManager: ConfigManager by inject()
    val connectivityManager: ConnectivityManager by inject()
    val currentState = MutableStateFlow<State>(State.Idle)

    //TechTask doesn't included this, but id needed, it's possible to update state from main screen
    fun updateState(isDownload: Boolean, newValue: Boolean) {
        configManager.setToggle(isDown = isDownload, newValue = newValue)
    }

    fun checkConnectivity(
        times: Int = 60,
    ) {
        currentState.value = State.Loading

        val nc = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val result = if (nc == null) {
            NetworkResult.Failure
        } else {
            NetworkResult.Success(
                downSpeed =
                if (configManager.isDownstreamEnabled.value)
                    nc.linkDownstreamBandwidthKbps
                else
                    null,
                upSpeed =
                if (configManager.isUpstreamEnabled.value)
                    nc.linkUpstreamBandwidthKbps
                else
                    null
            )
        }

        if (result is NetworkResult.Failure) {
            currentState.value = State.Error
        } else if (result is NetworkResult.Success) {
            currentState.value = State.Result(result)
        }
    }

    sealed class NetworkResult {
        data class Success(val downSpeed: Int?, val upSpeed: Int?) : NetworkResult()
        data object Failure : NetworkResult()
    }

    sealed class State {
        data object Idle : State()
        data object Loading : State()
        data class Result(val res: NetworkResult.Success) : State()
        data object Error : State()
    }
}