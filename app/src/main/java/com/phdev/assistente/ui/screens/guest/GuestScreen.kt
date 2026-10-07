package com.phdev.assistente.ui.screens.guest

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.phdev.assistente.R
import com.phdev.assistente.data.model.MediaItem
import com.phdev.assistente.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestScreen(
    onLogout: () -> Unit,
    onRequestDownload: (url: String, format: String, onResult: (Result<MediaItem>) -> Unit) -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                color = GlassSurface,
                shadowElevation = 0.dp
            ) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                imageVector = Icons.Rounded.ExitToApp,
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
        containerColor = GlassBackground
    ) { paddingValues ->
        MediaDownloaderContent(
            onRequestDownload = onRequestDownload,
            modifier = Modifier
                .padding(paddingValues)
                .background(brush = BackgroundGradientSimple)
        )
    }
}

@Composable
fun MediaDownloaderContent(
    onRequestDownload: (url: String, format: String, onResult: (Result<MediaItem>) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputUrl by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf("mp3") }
    var isLoading by remember { mutableStateOf(false) }
    var currentMedia by remember { mutableStateOf<MediaItem?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Entrance animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400, easing = EaseOutCubic)) +
                    slideInVertically(
                        initialOffsetY = { 20 },
                        animationSpec = tween(400, easing = EaseOutCubic)
                    )
        ) {
            // Main extraction card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = AccentLavender.copy(alpha = 0.08f)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = GlassCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassCardBorder)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    // Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    AccentLavender.copy(alpha = 0.1f),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Rounded.MusicNote,
                                contentDescription = null,
                                tint = AccentLavender,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Extração em alta fidelidade",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "YouTube, Instagram, TikTok · 320kbps com capa",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // URL input
                    OutlinedTextField(
                        value = inputUrl,
                        onValueChange = {
                            inputUrl = it
                            errorMessage = null
                        },
                        placeholder = {
                            Text(
                                "Cole o link aqui...",
                                color = TextMuted.copy(alpha = 0.6f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Link,
                                contentDescription = null,
                                tint = AccentLavenderSoft,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (inputUrl.isNotEmpty()) {
                                IconButton(onClick = { inputUrl = "" }) {
                                    Icon(
                                        Icons.Rounded.Close,
                                        contentDescription = "Limpar",
                                        tint = TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentLavenderSoft,
                            unfocusedBorderColor = GlassCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = Color(0x0DA78BFA),
                            unfocusedContainerColor = Color(0x08000000),
                            cursorColor = AccentLavender
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Format selector chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilterChip(
                            selected = selectedFormat == "mp3",
                            onClick = { selectedFormat = "mp3" },
                            label = {
                                Text(
                                    "🎵 MP3 · 320kbps",
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentLavender,
                                selectedLabelColor = TextOnAccent,
                                containerColor = Color(0x0D000000),
                                labelColor = TextPrimary.copy(alpha = 0.7f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = GlassCardBorder,
                                selectedBorderColor = AccentLavender,
                                enabled = true,
                                selected = selectedFormat == "mp3"
                            )
                        )

                        FilterChip(
                            selected = selectedFormat == "mp4",
                            onClick = { selectedFormat = "mp4" },
                            label = {
                                Text(
                                    "🎬 Vídeo MP4",
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentLavender,
                                selectedLabelColor = TextOnAccent,
                                containerColor = Color(0x0D000000),
                                labelColor = TextPrimary.copy(alpha = 0.7f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = GlassCardBorder,
                                selectedBorderColor = AccentLavender,
                                enabled = true,
                                selected = selectedFormat == "mp4"
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Download button
                    Button(
                        onClick = {
                            if (inputUrl.isNotBlank()) {
                                isLoading = true
                                errorMessage = null
                                var cleanUrl = inputUrl.trim()
                                if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
                                    cleanUrl = "https://" + cleanUrl.trimStart('/')
                                }
                                onRequestDownload(cleanUrl, selectedFormat) { result ->
                                    isLoading = false
                                    result.onSuccess { item ->
                                        currentMedia = item
                                        triggerAndroidDownload(context, item)
                                    }.onFailure { err ->
                                        errorMessage = err.localizedMessage ?: "Falha ao processar mídia"
                                    }
                                }
                            }
                        },
                        enabled = inputUrl.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentLavender,
                            contentColor = TextOnAccent,
                            disabledContainerColor = AccentLavender.copy(alpha = 0.3f),
                            disabledContentColor = TextOnAccent.copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = if (inputUrl.isNotBlank() && !isLoading) 6.dp else 0.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = TextOnAccent,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Extraindo...",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Icon(
                                Icons.Rounded.CloudDownload,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Processar download",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Error display
                    AnimatedVisibility(
                        visible = errorMessage != null,
                        enter = fadeIn(tween(200)) + expandVertically(tween(250)),
                        exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
                    ) {
                        errorMessage?.let {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = StatusOfflineBg
                            ) {
                                Text(
                                    text = it,
                                    color = StatusOffline,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Media result card
        AnimatedVisibility(
            visible = currentMedia != null,
            enter = fadeIn(tween(400)) +
                    slideInVertically(
                        initialOffsetY = { 30 },
                        animationSpec = tween(400, easing = EaseOutCubic)
                    ),
            exit = fadeOut(tween(200))
        ) {
            currentMedia?.let { media ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = AccentLavender.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    color = GlassCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        AccentLavender.copy(alpha = 0.2f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Thumbnail
                            AsyncImage(
                                model = media.thumbnail
                                    ?: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300",
                                contentDescription = "Capa",
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(
                                        1.dp,
                                        GlassCardBorder,
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = media.title,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${media.author ?: "Desconhecido"} · ${media.format.uppercase()}",
                                    color = AccentLavenderSoft,
                                    fontSize = 12.sp
                                )
                                if (media.fileSize != null) {
                                    Text(
                                        text = media.fileSize!!,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Re-download button
                        Button(
                            onClick = {
                                triggerAndroidDownload(context, media)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentMint,
                                contentColor = TextOnAccent
                            )
                        ) {
                            Icon(
                                Icons.Rounded.Download,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Salvar novamente",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// Triggers native Android download to Music or Downloads folder
private fun triggerAndroidDownload(context: Context, media: MediaItem) {
    try {
        val isMp3 = media.format.lowercase().contains("mp3")
        val destinationFolder = if (isMp3) {
            Environment.DIRECTORY_MUSIC
        } else {
            Environment.DIRECTORY_DOWNLOADS
        }

        val safeTitle = media.title.take(50).replace(Regex("[^a-zA-Z0-9.-]"), "_").trim('_')
        val ext = if (isMp3) "mp3" else if (media.format.lowercase().contains("mp4")) "mp4" else media.format.lowercase().trimStart('.')
        val fileName = "${safeTitle.ifEmpty { "audio" }}.$ext"

        val request = DownloadManager.Request(Uri.parse(media.downloadUrl))
            .setTitle(media.title)
            .setDescription("Salvando com capa e metadados...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(destinationFolder, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        val destName = if (isMp3) "Música" else "Downloads"
        Toast.makeText(context, "Download iniciado → pasta $destName", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Erro ao iniciar download: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
