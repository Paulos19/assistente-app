package com.phdev.assistente.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Warm Brown & Cream Palette (Light Modern Theme)
val LightBackground = Color(0xFFF9F7F4)       // Baunilha / Linho claro suave
val LightSurface = Color(0xFFFFFFFF)          // Branco puro para elevação/cards primários
val LightSurfaceAlt = Color(0xFFF2ECE4)       // Creme amendoado para contraste sutil
val LightCard = Color(0xFFFFFFFF)
val LightCardBorder = Color(0xFFE8DFD5)       // Borda quente e delicada
val LightCardBorderMuted = Color(0xFFF0EAE1)

// Marrons Nobres (Espresso, Caramelo, Castanho Quente)
val MochaPrimary = Color(0xFF4A2E18)         // Espresso marcante para títulos e botões principais
val MochaSecondary = Color(0xFF7C5335)       // Marrom intermediário aconchegante
val TerracottaAccent = Color(0xFFB85D38)     // Terracota vivo para destaques energéticos
val CaramelGlow = Color(0xFFD48B47)          // Dourado/Caramelo elegante para gradientes
val SandAccent = Color(0xFFEADBC8)           // Areia dourada para chips e backgrounds de tags

// Cores de Status Refinadas em Tons Terrosos / Quentes
val StatusSuccess = Color(0xFF2E6948)        // Verde floresta orgânico
val StatusSuccessBg = Color(0xFFEBF5EE)
val StatusError = Color(0xFFA83232)          // Vermelho terracota
val StatusErrorBg = Color(0xFFFDF0F0)
val StatusWarning = Color(0xFFC27803)        // Âmbar
val StatusWarningBg = Color(0xFFFEF7E6)

// Tipografia e Texto
val TextPrimary = Color(0xFF2B1B12)          // Marrom muito escuro / quase café para legibilidade máxima
val TextSecondary = Color(0xFF736055)        // Marrom neutro atenuado
val TextMuted = Color(0xFFA69488)            // Marrom suave para placeholders e notas
val TextOnDark = Color(0xFFFFFDFC)           // Branco quebrado para leitura em cima de botões escuros

// Gradientes Quentes de Alto Padrão
val WarmMochaGradient = Brush.linearGradient(
    colors = listOf(MochaPrimary, MochaSecondary)
)

val TerracottaGlowGradient = Brush.linearGradient(
    colors = listOf(TerracottaAccent, CaramelGlow)
)

val CardWarmSubtleGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFFFFF), Color(0xFFFAF7F2))
)

val HeaderWarmGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFF5EFE6), Color(0xFFF9F7F4))
)

// Aliases de compatibilidade para telas legadas (Dark Theme / Antigo design)
val DarkBackground = LightBackground
val DarkSurface = LightSurface
val DarkCard = LightCard
val DarkCardBorder = LightCardBorder

val AccentPrimary = MochaPrimary
val AccentSecondary = MochaSecondary
val AccentWarning = StatusWarning
val AccentSuccess = StatusSuccess
val AccentDanger = StatusError

// Esquema Material3
val AssistenteColorScheme = lightColorScheme(
    primary = MochaPrimary,
    onPrimary = TextOnDark,
    secondary = MochaSecondary,
    onSecondary = TextOnDark,
    tertiary = TerracottaAccent,
    background = LightBackground,
    surface = LightSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = LightSurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = LightCardBorder
)
