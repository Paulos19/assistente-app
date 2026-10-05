package com.phdev.assistente.data.api

import com.phdev.assistente.data.model.DownloadMediaRequest
import com.phdev.assistente.data.model.LoginRequest
import com.phdev.assistente.data.model.LoginResponse
import com.phdev.assistente.data.model.MediaItem
import com.phdev.assistente.data.model.SystemStatus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient(private val baseUrl: String = "https://agent.khdya3.easypanel.host") {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
        install(WebSockets)
    }

    private var authToken: String? = null

    fun setToken(token: String?) {
        authToken = token
    }

    suspend fun login(phone: String): LoginResponse {
        return try {
            client.post("$baseUrl/api/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(phone = phone))
            }.body()
        } catch (e: Exception) {
            LoginResponse(
                success = false,
                message = "Erro de conexão: ${e.localizedMessage ?: "Tente novamente"}"
            )
        }
    }

    suspend fun requestDownload(url: String, format: String): Result<MediaItem> {
        return try {
            val response: MediaItem = client.post("$baseUrl/api/media/download") {
                contentType(ContentType.Application.Json)
                authToken?.let { header("Authorization", "Bearer $it") }
                setBody(DownloadMediaRequest(url = url, format = format))
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSystemStatus(): Result<SystemStatus> {
        return try {
            val status: SystemStatus = client.get("$baseUrl/api/admin/status") {
                authToken?.let { header("Authorization", "Bearer $it") }
            }.body()
            Result.success(status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
