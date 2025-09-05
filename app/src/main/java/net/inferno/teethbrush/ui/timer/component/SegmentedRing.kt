package net.inferno.teethbrush.ui.timer.component

import androidx.annotation.FloatRange
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import net.inferno.teethbrush.theme.AppTheme

@Composable
fun SegmentedRing(
    @FloatRange(0.0, 360.0)
    value: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    segmentCount: Int = 4,
    segmentSweep: Float = 80f,
    @FloatRange(0.0, 45.0)
    segmentGap: Float = 5f,
    strokeWidth: Dp = 8.dp,
) {
    // 0 means center right
    // While -90 means top center
    val angle = value - 90

    val segment = remember(segmentCount) {
        360f / segmentCount
    }

    Canvas(
        modifier = modifier
    ) {
        for (i in 0 until segmentCount) {
            val startAngle = i * segment + segmentGap - 90f

            val sweep = (angle - startAngle).coerceIn(0f, segmentSweep)
            if (sweep > 0) {
                drawSector(
                    color = color,
                    startAngle = startAngle,
                    sweep = sweep,
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
}

fun DrawScope.drawGuideLines(
    color: Color = Color.Blue,
    strokeWidth: Float = 1f,
) {
    val centerX = size.center.x
    val centerY = size.center.y

    drawLine(
        color = color,
        start = Offset(0f, centerY),
        end = Offset(size.width, centerY),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = color,
        start = Offset(centerX, 0f),
        end = Offset(centerX, size.height),
        strokeWidth = strokeWidth
    )
}

fun DrawScope.drawSector(
    sweep: Float,
    startAngle: Float,
    color: Color,
    strokeWidth: Dp,
) {
    if (sweep > 0) {
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweep,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

@WearPreviewLargeRound
@Composable
fun SegmentedRing_Preview(
    @PreviewParameter(
        provider = AngleProvider::class,
    ) value: Float,
) {
    AppTheme {
        SegmentedRing(
            value,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            drawGuideLines()
        }
    }
}

@WearPreviewLargeRound
@Composable
fun SegmentedRing_PreviewIntermediate() {
    val infiniteTransition = rememberInfiniteTransition(
        label = "Ring Animation",
    )

    var initialValue by remember {
        mutableFloatStateOf(0f)
    }
    var targetValue by remember {
        mutableFloatStateOf(360f)
    }

    val value by infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Ring Animation",
    )

    AppTheme {
        SegmentedRing(
            value,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()
                .clickable {
                    val temp = initialValue
                    initialValue = targetValue
                    targetValue = temp
                }
        )
    }
}

class AngleProvider : PreviewParameterProvider<Float> {
    override val values = buildList {
        for (i in 0..360 / 30) {
            add(30f * i)
        }
    }.asSequence()
}