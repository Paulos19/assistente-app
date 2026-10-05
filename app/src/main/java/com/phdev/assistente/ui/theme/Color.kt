package com.phdev.assistente.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF090D16)
val DarkSurface = Color(0xFF131B2E)
val DarkCard = Color(0xFF1B243B)
val DarkCardBorder = Color(0xFF2E3D63)

val AccentPrimary = Color(0xFF6366F1) // Indigo tech
val AccentSecondary = Color(0xFF06B6D4) // Cyan neon
val AccentSuccess = Color(0xFF10B981) // Emerald
val AccentWarning = Color(0xFFF59E0B) // Amber
val AccentDanger = Color(0xFFEF4444) // Rose

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val AssistenteColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = Color.White,
    secondary = AccentSecondary,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    error = AccentDanger,
    onError = Color.White
)
