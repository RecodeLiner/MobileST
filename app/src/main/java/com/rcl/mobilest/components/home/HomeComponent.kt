package com.rcl.mobilest.components.home

import android.net.ConnectivityManager
import com.arkivanov.decompose.ComponentContext
import com.rcl.mobilest.di.config.ConfigManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HomeComponent(componentContext: ComponentContext) : ComponentContext by componentContext,
    KoinComponent {
    val configManager: ConfigManager by inject()
    val connectivityManager: ConnectivityManager by inject()
    val connectionState = MutableStateFlow<NetworkResult>(NetworkResult.Success(0, 0))
    val currentState = MutableStateFlow<State>(State.Idle)

    private var downSpeedSum: Long = 0
    private var upSpeedSum: Long = 0
    private var measurementCount: Int = 0

    fun startMeasurement() {
        downSpeedSum = 0
        upSpeedSum = 0
        measurementCount = 0
        currentState.value = State.Loading(0f)
        CoroutineScope(Dispatchers.Main.immediate).launch {
            repeat(60) {
                currentState.value = State.Loading(((it/60*100).toFloat()))
                checkConnectivity()
                delay(1000)
            }
            val averageDownSpeed = if (measurementCount > 0) downSpeedSum / measurementCount else null
            val averageUpSpeed = if (measurementCount > 0) upSpeedSum / measurementCount else null
            connectionState.value = NetworkResult.Success(averageDownSpeed, averageUpSpeed)
            currentState.value = State.Result
        }
    }

    private fun checkConnectivity() {
        val nc = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val result = if (nc == null) {
            NetworkResult.Failure
        } else {
            NetworkResult.Success(
                downSpeed =
                if (configManager.isDownstreamEnabled.value)
                    nc.linkDownstreamBandwidthKbps.toLong()
                else
                    null,
                upSpeed =
                if (configManager.isUpstreamEnabled.value)
                    nc.linkUpstreamBandwidthKbps.toLong()
                else
                    null
            )
        }

        if (result is NetworkResult.Success) {
            result.downSpeed?.let { downSpeedSum += it }
            result.upSpeed?.let { upSpeedSum += it }
            measurementCount++
            currentState.value = State.Loading(measurementCount.toFloat() / 60)
            connectionState.value = result
        } else {
            currentState.value = State.Error
        }
    }

    sealed class NetworkResult {
        data class Success(val downSpeed: Long?, val upSpeed: Long?) : NetworkResult()
        data object Failure : NetworkResult()
    }

    sealed class State {
        data object Idle : State()
        data class Loading(val progress: Float) : State()
        data object Result : State()
        data object Error : State()
    }
}
