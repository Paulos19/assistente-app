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
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.timeout
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApiClient(private val baseUrl: String = "https://agent.phdev.top") {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 300_000 // 5 minutos
            connectTimeoutMillis = 60_000  // 60s
            socketTimeoutMillis = 300_000  // 5 minutos
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
            var normalizedUrl = url.trim()
            if (!normalizedUrl.startsWith("http://") && !normalizedUrl.startsWith("https://")) {
                normalizedUrl = "https://" + normalizedUrl.trimStart('/')
            }

            val httpResponse = client.post("$baseUrl/api/media/download") {
                timeout {
                    requestTimeoutMillis = 300_000
                    connectTimeoutMillis = 60_000
                    socketTimeoutMillis = 300_000
                }
                contentType(ContentType.Application.Json)
                authToken?.let { header("Authorization", "Bearer $it") }
                setBody(DownloadMediaRequest(url = normalizedUrl, format = format))
            }

            if (httpResponse.status.value in 200..299) {
                Result.success(httpResponse.body<MediaItem>())
            } else {
                val errorBody = httpResponse.bodyAsText()
                val errorMsg = try {
                    val jsonElem = Json.parseToJsonElement(errorBody)
                    jsonElem.jsonObject["detail"]?.jsonPrimitive?.content
                        ?: jsonElem.jsonObject["message"]?.jsonPrimitive?.content
                        ?: jsonElem.jsonObject["error"]?.jsonPrimitive?.content
                        ?: "Erro no servidor (${httpResponse.status.value})"
                } catch (_: Exception) {
                    "Erro no servidor (${httpResponse.status.value})"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSystemStatus(): Result<SystemStatus> {
        return try {
            val response = client.get("$baseUrl/api/admin/status") {
                authToken?.let { header("Authorization", "Bearer $it") }
            }
            if (response.status.value in 200..299) {
                Result.success(response.body<SystemStatus>())
            } else {
                Result.failure(Exception("Falha ao obter status (${response.status.value})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
