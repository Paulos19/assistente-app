package com.phdev.assistente.ui.screens.admin

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phdev.assistente.R
import com.phdev.assistente.data.model.ChatMessage
import com.phdev.assistente.data.model.MediaItem
import com.phdev.assistente.data.model.SystemStatus
import com.phdev.assistente.ui.screens.guest.MediaDownloaderContent
import com.phdev.assistente.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onLogout: () -> Unit,
    status: SystemStatus?,
    onActionClick: (String) -> Unit,
    onRequestDownload: (url: String, format: String, onResult: (Result<MediaItem>) -> Unit) -> Unit = { _, _, _ -> }
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Surface(
                color = GlassSurface,
                shadowElevation = 0.dp
            ) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Logo mark
                            Image(
                                painter = painterResource(id = R.drawable.ic_agente_logo),
                                contentDescription = "Logo Agente",
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Agente",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                letterSpacing = (-0.3).sp
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(
                                Icons.Rounded.ExitToApp,
                                contentDescription = "Sair",
                                tint = TextMuted
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        bottomBar = {
            Surface(
                color = GlassSurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color(0x1A000000)
                )
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                Icons.Rounded.Dashboard,
                                contentDescription = "Painel"
                            )
                        },
                        label = { Text("Painel", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentLavender,
                            selectedTextColor = AccentLavender,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = AccentLavender.copy(alpha = 0.1f)
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                Icons.Rounded.CloudDownload,
                                contentDescription = "Mídia"
                            )
                        },
                        label = { Text("Mídia", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentLavender,
                            selectedTextColor = AccentLavender,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = AccentLavender.copy(alpha = 0.1f)
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(
                                Icons.Rounded.Terminal,
                                contentDescription = "Agente"
                            )
                        },
                        label = { Text("Agente", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentLavender,
                            selectedTextColor = AccentLavender,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = AccentLavender.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
        containerColor = GlassBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(brush = BackgroundGradientSimple)
        ) {
            // Tab content with crossfade animation
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(tween(300, easing = EaseOutCubic)) togetherWith
                            fadeOut(tween(200, easing = EaseInCubic))
                },
                label = "tabContent"
            ) { tab ->
                when (tab) {
                    0 -> AdminDashboardTab(status = status, onActionClick = onActionClick)
                    1 -> MediaDownloaderContent(onRequestDownload = onRequestDownload)
                    else -> AdminAgentChatTab()
                }
            }
        }
    }
}

@Composable
fun AdminDashboardTab(
    status: SystemStatus?,
    onActionClick: (String) -> Unit
) {
    // Staggered entrance animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(50)
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Section: Infrastructure Status
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400, delayMillis = 0, easing = EaseOutCubic)) +
                    slideInVertically(
                        initialOffsetY = { 20 },
                        animationSpec = tween(400, easing = EaseOutCubic)
                    )
        ) {
            Column {
                Text(
                    "Infraestrutura",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassMetricCard(
                        title = "VPS Cloud",
                        value = "${status?.vpsCpuPercent?.toInt() ?: 12}%",
                        valueLabel = "CPU",
                        subtitle = "RAM: ${status?.vpsMemoryPercent?.toInt() ?: 38}%",
                        isOnline = true,
                        icon = Icons.Rounded.CloudQueue,
                        modifier = Modifier.weight(1f)
                    )
                    GlassMetricCard(
                        title = "PC Windows",
                        value = if (status?.pcConnected != false) "On" else "Off",
                        valueLabel = "",
                        subtitle = status?.pcHostName ?: "DESKTOP",
                        isOnline = status?.pcConnected != false,
                        icon = Icons.Rounded.Computer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Section: Quick Actions
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400, delayMillis = 100, easing = EaseOutCubic)) +
                    slideInVertically(
                        initialOffsetY = { 20 },
                        animationSpec = tween(400, delayMillis = 100, easing = EaseOutCubic)
                    )
        ) {
            Column {
                Text(
                    "Controle do PC",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassActionButton(
                        label = "Capturar",
                        icon = Icons.Rounded.Screenshot,
                        accentColor = AccentSky,
                        modifier = Modifier.weight(1f),
                        onClick = { onActionClick("screenshot") }
                    )
                    GlassActionButton(
                        label = "Bloquear",
                        icon = Icons.Rounded.Lock,
                        accentColor = StatusWarning,
                        modifier = Modifier.weight(1f),
                        onClick = { onActionClick("lock") }
                    )
                    GlassActionButton(
                        label = "Suspender",
                        icon = Icons.Rounded.PowerSettingsNew,
                        accentColor = StatusOffline,
                        modifier = Modifier.weight(1f),
                        onClick = { onActionClick("suspend") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Section: CI/CD Status
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400, delayMillis = 200, easing = EaseOutCubic)) +
                    slideInVertically(
                        initialOffsetY = { 20 },
                        animationSpec = tween(400, delayMillis = 200, easing = EaseOutCubic)
                    )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = AccentLavender.copy(alpha = 0.06f)
                    ),
                shape = RoundedCornerShape(20.dp),
                color = GlassCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                AccentMint.copy(alpha = 0.12f),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.RocketLaunch,
                            contentDescription = null,
                            tint = AccentMint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            "Easypanel & CI/CD",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "Serviço ativo na porta 8000 · Worker conectado",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassMetricCard(
    title: String,
    value: String,
    valueLabel: String,
    subtitle: String,
    isOnline: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = AccentLavender.copy(alpha = 0.06f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = GlassCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassCardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            AccentLavender.copy(alpha = 0.08f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = AccentLavenderSoft,
                        modifier = Modifier.size(18.dp)
                    )
                }
                // Online indicator with pulse
                val pulseAlpha = if (isOnline) {
                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = EaseInOutSine),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulseAlpha"
                    )
                    alpha
                } else 1f

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            if (isOnline) StatusOnline.copy(alpha = pulseAlpha)
                            else StatusOffline
                        )
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                title,
                color = TextMuted,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    value,
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                if (valueLabel.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        valueLabel,
                        color = TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                subtitle,
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun GlassActionButton(
    label: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = GlassCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassCardBorder)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        accentColor.copy(alpha = 0.1f),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun AdminAgentChatTab() {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    id = "1",
                    text = "Agente pronto. Como posso ajudar com a VPS ou com o PC?",
                    isFromUser = false
                )
            )
        )
    }
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages.reversed()) { msg ->
                GlassChatBubble(message = msg)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Chat input bar
        Surface(
            color = GlassSurface,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0x1A000000)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Enviar instrução ao agente...",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentLavenderSoft,
                        unfocusedBorderColor = GlassCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = AccentLavender
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val userMsg = ChatMessage(
                                id = System.currentTimeMillis().toString(),
                                text = inputText,
                                isFromUser = true
                            )
                            messages = messages + userMsg
                            inputText = ""
                            messages = messages + ChatMessage(
                                id = (System.currentTimeMillis() + 1).toString(),
                                text = "Comando recebido. Processando...",
                                isFromUser = false
                            )
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = AccentLavender,
                        contentColor = TextOnAccent
                    ),
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        Icons.Rounded.Send,
                        contentDescription = "Enviar",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GlassChatBubble(message: ChatMessage) {
    val isUser = message.isFromUser
    val alignment = if (isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = if (isUser) AccentLavender else GlassCard,
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            border = if (!isUser) {
                androidx.compose.foundation.BorderStroke(1.dp, GlassCardBorder)
            } else null,
            shadowElevation = if (!isUser) 2.dp else 4.dp
        ) {
            Text(
                text = message.text,
                color = if (isUser) TextOnAccent else TextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 280.dp)
            )
        }
    }
}
