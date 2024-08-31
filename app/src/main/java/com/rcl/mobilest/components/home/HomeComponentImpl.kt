package com.rcl.mobilest.components.home

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rcl.mobilest.R

@Composable
fun HomeComponentImpl(homeComponent: HomeComponent) {
    val isUpstreamEnabled by homeComponent.configManager.isUpstreamEnabled.collectAsState()
    val isDownstreamEnabled by homeComponent.configManager.isDownstreamEnabled.collectAsState()
    val state by homeComponent.currentState.collectAsState()

    val context = LocalContext.current

    AnimatedContent(state, label = "") {
        when (it) {
            is HomeComponent.State.Idle -> {
                RestartButton(text = R.string.start_measure, checkConnectivity = {
                    homeComponent.checkConnectivity(
                        context = context,
                        isDownAvailable = isDownstreamEnabled,
                        isUpAvailable = isUpstreamEnabled
                    )
                })
            }

            is HomeComponent.State.Result -> {
                Column {
                    Text(
                        stringResource(
                            id = R.string.up_speed_res,
                            (if (isUpstreamEnabled) ((state as HomeComponent.State.Result).res.upSpeed?.div(
                                1024
                            )).toString() else stringResource(
                                R.string.unavailable
                            ))
                        )
                    )
                    Text(
                        stringResource(
                            id = R.string.down_speed_res,
                            (if (isDownstreamEnabled) ((state as HomeComponent.State.Result).res.downSpeed?.div(
                                1024
                            )).toString() else stringResource(
                                R.string.unavailable
                            ))
                        )
                    )

                    RestartButton(text = R.string.restart, checkConnectivity = {
                        homeComponent.checkConnectivity(
                            context = context,
                            isDownAvailable = isDownstreamEnabled,
                            isUpAvailable = isUpstreamEnabled
                        )
                    })
                }
            }

            is HomeComponent.State.Error -> {
                Column {
                    Text(
                        text = stringResource(R.string.error)
                    )

                    RestartButton(text = R.string.restart, checkConnectivity = {
                        homeComponent.checkConnectivity(
                            context = context,
                            isDownAvailable = isDownstreamEnabled,
                            isUpAvailable = isUpstreamEnabled
                        )
                    })
                }
            }

            is HomeComponent.State.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun RestartButton(@StringRes text: Int, checkConnectivity: () -> Unit) {
    Button(modifier = Modifier.clip(CircleShape), onClick = {
        checkConnectivity()
    }) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(text)
        )
    }
}