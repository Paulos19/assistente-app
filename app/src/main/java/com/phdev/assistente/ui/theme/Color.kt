package com.phdev.assistente.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme

// ─── Glassmorphism Ethereal Palette ───────────────────────────────────────────
// Inspired by frosted glass, soft pastels, and translucent light

// Background Gradients
val GlassBackground = Color(0xFFF0EEFF)         // Lavender mist
val GlassMint = Color(0xFFE8FBF3)                // Soft mint
val GlassSky = Color(0xFFE6F0FF)                 // Pale sky
val GlassLavender = Color(0xFFEDE8FF)             // Light lavender

// Surface & Cards (Frosted Glass)
val GlassSurface = Color(0xCCFFFFFF)              // White at 80% opacity
val GlassSurfaceAlt = Color(0x99FFFFFF)           // White at 60% opacity
val GlassCard = Color(0xB3FFFFFF)                 // White at 70% opacity
val GlassCardBorder = Color(0x33B4A5E8)           // Lavender border at 20%
val GlassCardBorderFocused = Color(0x66A78BFA)    // Lavender border focused

// Accent Colors (Gradient Spectrum)
val AccentLavender = Color(0xFF8B5CF6)            // Vibrant lavender
val AccentLavenderSoft = Color(0xFFA78BFA)        // Soft lavender
val AccentSky = Color(0xFF60A5FA)                 // Sky blue
val AccentMint = Color(0xFF34D399)                // Emerald mint
val AccentRose = Color(0xFFFB7185)                // Soft rose

// Status Colors
val StatusOnline = Color(0xFF34D399)              // Mint green
val StatusOnlineBg = Color(0x1A34D399)
val StatusOffline = Color(0xFFFB7185)             // Rose
val StatusOfflineBg = Color(0x1AFB7185)
val StatusWarning = Color(0xFFFBBF24)             // Amber
val StatusWarningBg = Color(0x1AFBBF24)

// Text Hierarchy
val TextPrimary = Color(0xFF1E1B4B)               // Deep indigo
val TextSecondary = Color(0xFF6366F1).copy(alpha = 0.6f) // Indigo muted
val TextMuted = Color(0xFF9CA3AF)                 // Cool gray
val TextOnAccent = Color(0xFFFFFFFF)              // Pure white

// Gradients
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(AccentLavender, AccentSky)
)

val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        GlassLavender,
        GlassSky,
        GlassMint,
        GlassLavender
    )
)

val BackgroundGradientSimple = Brush.verticalGradient(
    colors = listOf(
        GlassLavender,
        GlassSky
    )
)

val CardGlowGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x0DA78BFA),
        Color(0x00A78BFA)
    )
)

val AccentButtonGradient = Brush.horizontalGradient(
    colors = listOf(AccentLavender, AccentSky, AccentMint)
)

val SubtleGlassGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x40FFFFFF),
        Color(0x1AFFFFFF)
    )
)

// ── Legacy Aliases (backward compat) ──────────────────────────────────────────
val DarkBackground = GlassBackground
val DarkSurface = GlassSurface
val DarkCard = GlassCard
val DarkCardBorder = GlassCardBorder
val LightBackground = GlassBackground
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceAlt = GlassSky

val AccentPrimary = AccentLavender
val AccentSecondary = AccentSky
val AccentWarning = StatusWarning
val AccentSuccess = StatusOnline
val AccentDanger = StatusOffline

val MochaPrimary = AccentLavender
val MochaSecondary = AccentSky
val TerracottaAccent = AccentRose
val SandAccent = Color(0xFFEDE8FF)
val TextOnDark = TextOnAccent

val WarmMochaGradient = PrimaryGradient

// ── Material 3 Color Scheme ───────────────────────────────────────────────────
val AssistenteColorScheme = lightColorScheme(
    primary = AccentLavender,
    onPrimary = TextOnAccent,
    secondary = AccentSky,
    onSecondary = TextOnAccent,
    tertiary = AccentMint,
    background = GlassBackground,
    surface = Color(0xFFFFFFFF),
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = GlassSky,
    onSurfaceVariant = TextPrimary.copy(alpha = 0.7f),
    outline = GlassCardBorder
)
