package net.inferno.teethbrush.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import net.inferno.teethbrush.theme.AppTheme
import net.inferno.teethbrush.ui.main.MainViewModel
import net.inferno.teethbrush.ui.setting.component.MinutesPicker

@Composable
fun SettingUI(
    mainViewModel: MainViewModel = hiltViewModel(),
    onSubmit: () -> Unit,
) {
    val duration by mainViewModel.durationFlow.collectAsStateWithLifecycle()

    if (duration != null) {
        SettingUI(
            startValue = duration!!,
            onSubmit = {
                mainViewModel.setDuration(it)
                onSubmit()
            },
            modifier = Modifier,
        )
    } else {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SettingUI(
    modifier: Modifier = Modifier,
    startValue: Int = 0,
    onSubmit: (Int) -> Unit,
) {
    val locale = Locale.current.platformLocale

    var currentValue by remember {
        mutableIntStateOf(startValue)
    }

    Box {
        MinutesPicker(
            valueInSeconds = currentValue,
            onValueChange = {
                currentValue = it
            },
            stepSeconds = 20,
        )

        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val minutes = currentValue / 60
            val seconds = currentValue % 60

            Spacer(Modifier.weight(1f))

            Text(
                text = String.format(
                    locale,
                    "%02d:%02d", minutes, seconds,
                ),
                fontSize = 48.sp,
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = {
                        onSubmit(currentValue)
                    },
                ) {
                    Text("Set")
                }
            }
        }
    }
}

@WearPreviewLargeRound
@Composable
fun SettingUI_Preview() {
    AppTheme {
        SettingUI(
            onSubmit = {},
            modifier = Modifier,
        )
    }
}