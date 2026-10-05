package com.phdev.assistente.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val phone: String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val role: String? = null, // "admin" ou "guest"
    val name: String? = null,
    val message: String? = null
)

@Serializable
data class DownloadMediaRequest(
    val url: String,
    val format: String, // "mp3" ou "mp4"
    val quality: String = "best"
)

@Serializable
data class MediaItem(
    val id: String,
    val title: String,
    val author: String? = null,
    val duration: String? = null,
    val thumbnail: String? = null,
    val downloadUrl: String,
    val format: String,
    val fileSize: String? = null
)

@Serializable
data class SystemStatus(
    val vpsCpuPercent: Double,
    val vpsMemoryPercent: Double,
    val pcConnected: Boolean,
    val pcHostName: String? = null,
    val activeTasks: Int = 0
)

@Serializable
data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
