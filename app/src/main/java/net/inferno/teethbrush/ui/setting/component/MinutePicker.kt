package net.inferno.teethbrush.ui.setting.component

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import kotlin.math.sign

@Composable
fun MinutesPicker(
    valueInSeconds: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    stepSeconds: Int = 30,
    minSeconds: Int = 2 * 60, // 2 minutes
    maxSeconds: Int = 10 * 60, // 10 minutes
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onRotaryScrollEvent { event ->
                val delta = event.verticalScrollPixels

                Log.d("Rotary Event", event.verticalScrollPixels.toString())

                val newValue = (valueInSeconds + stepSeconds * delta.sign.toInt())
                    .coerceIn(minSeconds, maxSeconds)
                if (newValue != valueInSeconds) {
                    onValueChange(newValue)
                }

                true
            }
            .focusRequester(focusRequester)
            .focusable(),
    )
}