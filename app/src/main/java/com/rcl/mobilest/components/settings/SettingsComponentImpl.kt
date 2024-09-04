package com.rcl.mobilest.components.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rcl.mobilest.R
import com.rcl.mobilest.components.settings.SettingsComponent.DarkThemeEnum

@Composable
fun SettingsComponentImpl(settingsComponent: SettingsComponent) {
    val isUpstreamEnabled by settingsComponent.configManager.isUpstreamEnabled.collectAsState()
    val isDownstreamEnabled by settingsComponent.configManager.isDownstreamEnabled.collectAsState()
    val themeManager = settingsComponent.themeManager
    val isDarkTheme by themeManager.isDarkTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AvailabilityCard(
            textId = R.string.upstream,
            state = isUpstreamEnabled,
            changeState = {
                settingsComponent.updateState(
                    isDownload = false,
                    newValue = !isUpstreamEnabled
                )
            }
        )

        AvailabilityCard(
            textId = R.string.downstream,
            state = isDownstreamEnabled,
            changeState = {
                settingsComponent.updateState(
                    isDownload = true,
                    newValue = !isDownstreamEnabled
                )
            }
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(stringResource(R.string.theme_title))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DarkThemeEnum.entries.forEach { state ->
                val selected = state.value == isDarkTheme
                SelectableCard(
                    selected = selected,
                    value = state.value,
                    onClick = {
                        themeManager.setDarkThemeValue(it)
                    },
                    text = state.id
                )
            }
        }
    }
}

@Composable
private fun SelectableCard(
    selected: Boolean,
    value: Boolean?,
    onClick: (Boolean?) -> Unit,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    val cardColor = if (selected) {
        CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        CardDefaults.cardColors()
    }
    Card(
        colors = cardColor,
        modifier = modifier.clickable { onClick(value) }
    ) {
        Text(
            text = stringResource(text),
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AvailabilityCard(
    @StringRes textId: Int,
    state: Boolean,
    changeState: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = changeState),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(textId))
            Switch(
                checked = state,
                onCheckedChange = { changeState() }
            )
        }
    }
}