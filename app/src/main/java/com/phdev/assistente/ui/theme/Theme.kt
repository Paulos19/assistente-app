package com.phdev.assistente.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AssistenteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AssistenteColorScheme,
        content = content
    )
}
