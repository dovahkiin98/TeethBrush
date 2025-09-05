package net.inferno.teethbrush.ui.timer

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import net.inferno.teethbrush.theme.AppTheme
import net.inferno.teethbrush.ui.main.MainViewModel
import net.inferno.teethbrush.ui.timer.component.SegmentedRing
import java.util.concurrent.TimeUnit

@Composable
fun TimerUI(
    mainViewModel: MainViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
) {
    val duration by mainViewModel.durationFlow.collectAsStateWithLifecycle()

    println(duration)

    if (duration != null) {
        TimerUI(
            duration = duration!!,
            onNavigateToSettings = onNavigateToSettings,
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
private fun TimerUI(
    duration: Int,
    onNavigateToSettings: () -> Unit,
) {
    val context = LocalContext.current
    val locale = Locale.current.platformLocale

    val vibrator = remember { context.getSystemService<Vibrator>()!! }

    var startAnimation by remember(duration) { mutableStateOf(false) }
    val animation by animateFloatAsState(
        if (startAnimation) 0f else 360f,
        animationSpec = if (startAnimation) tween(
            durationMillis = duration * 1000,
            easing = LinearEasing,
        ) else snap(),
        label = "Ring Animation",
    )

    val vibrate by remember {
        derivedStateOf {
            animation < 360f && (animation % 90f).toInt() == 0
        }
    }

    val seconds by remember(duration) {
        derivedStateOf {
            (animation * duration / 360).toLong() * 1_000
        }
    }

    LaunchedEffect(vibrate) {
        if (vibrate) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        val minutesValue = TimeUnit.MILLISECONDS.toMinutes(seconds)
        val secondsValue =
            TimeUnit.MILLISECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(minutesValue)

        Text(
            text = String.format(
                locale,
                "%02d:%02d", minutesValue, secondsValue,
            ),
            fontSize = 48.sp,
        )
    }

    SegmentedRing(
        value = animation,
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    startAnimation = !startAnimation
                },
                onLongClick = {
                    if (!startAnimation) {
                        onNavigateToSettings()
                    }
                },
            ),
    )
}

@WearPreviewLargeRound
@Composable
fun TimerUI_Preview() {
    AppTheme {
        TimerUI(
            duration = 2 * 60,
            onNavigateToSettings = {},
        )
    }
}