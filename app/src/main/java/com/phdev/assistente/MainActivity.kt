package com.phdev.assistente

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phdev.assistente.data.api.ApiClient
import com.phdev.assistente.data.model.SystemStatus
import com.phdev.assistente.ui.screens.admin.AdminScreen
import com.phdev.assistente.ui.screens.guest.GuestScreen
import com.phdev.assistente.ui.screens.login.LoginScreen
import com.phdev.assistente.ui.theme.AssistenteTheme
import com.phdev.assistente.ui.theme.DarkBackground
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val apiClient by lazy { ApiClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssistenteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    AppNavigation(
                        apiClient = apiClient,
                        onShowToast = { msg ->
                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    apiClient: ApiClient,
    onShowToast: (String) -> Unit
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    var isLoggingIn by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var systemStatus by remember { mutableStateOf<SystemStatus?>(null) }

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        composable("login") {
            LoginScreen(
                isLoading = isLoggingIn,
                errorMessage = loginError,
                onLoginClick = { phone ->
                    isLoggingIn = true
                    loginError = null
                    scope.launch {
                        val response = apiClient.login(phone)
                        isLoggingIn = false
                        if (response.success) {
                            apiClient.setToken(response.token)
                            val userRole = response.role?.lowercase() ?: "guest"
                            if (userRole == "admin") {
                                // Carrega status inicial do sistema para o Admin
                                val statusResult = apiClient.getSystemStatus()
                                systemStatus = statusResult.getOrNull()
                                navController.navigate("admin") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("guest") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        } else {
                            loginError = response.message ?: "Telefone não autorizado ou erro no servidor."
                        }
                    }
                }
            )
        }

        composable("admin") {
            AdminScreen(
                onLogout = {
                    apiClient.setToken(null)
                    systemStatus = null
                    navController.navigate("login") {
                        popUpTo("admin") { inclusive = true }
                    }
                },
                status = systemStatus,
                onActionClick = { action ->
                    onShowToast("Ação solicitada: $action")
                    if (action == "refresh_status") {
                        scope.launch {
                            val res = apiClient.getSystemStatus()
                            res.onSuccess { systemStatus = it }
                        }
                    }
                }
            )
        }

        composable("guest") {
            GuestScreen(
                onLogout = {
                    apiClient.setToken(null)
                    navController.navigate("login") {
                        popUpTo("guest") { inclusive = true }
                    }
                },
                onRequestDownload = { url, format, onResult ->
                    scope.launch {
                        val result = apiClient.requestDownload(url, format)
                        onResult(result)
                    }
                }
            )
        }
    }
}
