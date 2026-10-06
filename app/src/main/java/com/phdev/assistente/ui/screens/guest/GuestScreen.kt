package com.phdev.assistente.ui.screens.guest

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(AccentSuccess)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Media Downloader",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Rounded.ExitToApp,
                            contentDescription = "Sair",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        MediaDownloaderContent(
            onRequestDownload = onRequestDownload,
            modifier = Modifier.padding(paddingValues)
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
    var selectedFormat by remember { mutableStateOf("mp3") } // "mp3" ou "mp4"
    var isLoading by remember { mutableStateOf(false) }
    var currentMedia by remember { mutableStateOf<MediaItem?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Card informativo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Extração em Alta Fidelidade",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cole o link do YouTube, Instagram Reels ou TikTok. O áudio é convertido em 320kbps com capa embutida!",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = inputUrl,
                        onValueChange = {
                            inputUrl = it
                            errorMessage = null
                        },
                        placeholder = { Text("https://www.youtube.com/watch?v=...") },
                        leadingIcon = {
                            Icon(Icons.Rounded.Link, contentDescription = null, tint = AccentSecondary)
                        },
                        trailingIcon = {
                            if (inputUrl.isNotEmpty()) {
                                IconButton(onClick = { inputUrl = "" }) {
                                    Icon(Icons.Rounded.Close, contentDescription = "Limpar", tint = TextMuted)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentPrimary,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Seleção de Formato (MP3 / MP4)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilterChip(
                            selected = selectedFormat == "mp3",
                            onClick = { selectedFormat = "mp3" },
                            label = { Text("🎵 Áudio MP3 (320kbps)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = DarkCard,
                                labelColor = TextSecondary
                            )
                        )

                        FilterChip(
                            selected = selectedFormat == "mp4",
                            onClick = { selectedFormat = "mp4" },
                            label = { Text("🎬 Vídeo MP4") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = DarkCard,
                                labelColor = TextSecondary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (inputUrl.isNotBlank()) {
                                isLoading = true
                                errorMessage = null
                                var cleanUrl = inputUrl.trim()
                                if (cleanUrl.startsWith("//")) {
                                    cleanUrl = "https:$cleanUrl"
                                } else if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
                                    cleanUrl = "https://$cleanUrl"
                                }
                                onRequestDownload(cleanUrl, selectedFormat) { result ->
                                    isLoading = false
                                    result.onSuccess { item ->
                                        currentMedia = item
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
                            containerColor = AccentPrimary,
                            disabledContainerColor = DarkCardBorder
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Extraindo da Nuvem...")
                        } else {
                            Icon(Icons.Rounded.CloudDownload, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Processar Download", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    AnimatedVisibility(visible = errorMessage != null) {
                        errorMessage?.let {
                            Text(
                                text = it,
                                color = AccentDanger,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Resultado do Download (Card de Mídia com Capa)
            AnimatedVisibility(visible = currentMedia != null) {
                currentMedia?.let { media ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, AccentPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Capa
                                AsyncImage(
                                    model = media.thumbnail ?: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300",
                                    contentDescription = "Capa",
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = media.title,
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${media.author ?: "Desconhecido"} • ${media.format.uppercase()}",
                                        color = AccentSecondary,
                                        fontSize = 12.sp
                                    )
                                    if (media.fileSize != null) {
                                        Text(
                                            text = "Tamanho: ${media.fileSize}",
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botão para salvar direto na pasta do Android
                            Button(
                                onClick = {
                                    triggerAndroidDownload(context, media)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentSuccess)
                            ) {
                                Icon(Icons.Rounded.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Salvar no Celular (${if (media.format == "mp3") "Música" else "Downloads"})",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// Dispara o download nativo do Android
private fun triggerAndroidDownload(context: Context, media: MediaItem) {
    try {
        val destinationFolder = if (media.format.lowercase() == "mp3") {
            Environment.DIRECTORY_MUSIC
        } else {
            Environment.DIRECTORY_DOWNLOADS
        }

        val fileName = "${media.title.take(40).replace(Regex("[^a-zA-Z0-9.-]"), "_")}.${media.format}"

        val request = DownloadManager.Request(Uri.parse(media.downloadUrl))
            .setTitle(media.title)
            .setDescription("Baixando via Assistente...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(destinationFolder, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        Toast.makeText(context, "Download iniciado! Verifique suas notificações.", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Erro ao baixar: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
