package net.inferno.teethbrush.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content,
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF991600),
            background = Color.Black,
            onPrimary = Color.White,
        )
    )
}