package com.rcl.mobilest.components.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rcl.mobilest.R

@Composable
fun HomeComponentImpl(homeComponent: HomeComponent) {
    val state by homeComponent.currentState.collectAsState()
    val connection by homeComponent.connectionState.collectAsState()

    when (state) {
        is HomeComponent.State.Idle -> {
            RestartButton(text = R.string.start_measure, checkConnectivity = {
                homeComponent.startMeasurement()
            })
        }

        is HomeComponent.State.Result -> {
            Column {
                Text(
                    if ((connection as HomeComponent.NetworkResult.Success).downSpeed != null ) {
                        stringResource(
                            id = R.string.down_speed_average_res,
                            (connection as HomeComponent.NetworkResult.Success).downSpeed?.div(
                                1024
                            ).toString()
                        )
                    } else {
                        stringResource(
                            id = R.string.unavailable,
                            stringResource(R.string.downstream)
                        )
                    }
                )
                Text(
                    if ((connection as HomeComponent.NetworkResult.Success).upSpeed != null) {
                        stringResource(
                            id = R.string.up_speed_average_res,
                            (connection as HomeComponent.NetworkResult.Success).upSpeed?.div(
                                1024
                            ).toString()
                        )
                    } else {
                        stringResource(
                            id = R.string.unavailable,
                            stringResource(R.string.upstream)
                        )
                    }
                )

                RestartButton(text = R.string.restart, checkConnectivity = {
                    homeComponent.startMeasurement()
                })
            }
        }

        is HomeComponent.State.Error -> {
            Column {
                Text(
                    text = stringResource(R.string.error)
                )

                RestartButton(text = R.string.restart, checkConnectivity = {
                    homeComponent.startMeasurement()
                })
            }
        }

        is HomeComponent.State.Loading -> {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CircularProgressIndicator(
                    progress = { (state as HomeComponent.State.Loading).progress }
                )

                Column {
                    Text(
                        if ((connection as HomeComponent.NetworkResult.Success).downSpeed != null) {
                            stringResource(
                                id = R.string.down_speed_res,
                                ((connection as HomeComponent.NetworkResult.Success).downSpeed!!/1024).toString()
                            )
                        } else {
                            stringResource(
                                id = R.string.unavailable,
                                stringResource(R.string.downstream)
                            )
                        }
                    )
                    Text(
                        if ((connection as HomeComponent.NetworkResult.Success).upSpeed != null) {
                            stringResource(
                                id = R.string.up_speed_res,
                                ((connection as HomeComponent.NetworkResult.Success).upSpeed!!/1024).toString()
                            )
                        } else {
                            stringResource(
                                id = R.string.unavailable,
                                stringResource(R.string.upstream)
                            )
                        }
                    )
                }
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