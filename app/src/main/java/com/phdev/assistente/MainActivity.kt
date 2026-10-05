package com.phdev.assistente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phdev.assistente.data.model.UserRole
import com.phdev.assistente.ui.screens.admin.AdminScreen
import com.phdev.assistente.ui.screens.guest.GuestScreen
import com.phdev.assistente.ui.screens.login.LoginScreen
import com.phdev.assistente.ui.theme.AssistenteTheme
import com.phdev.assistente.ui.theme.DarkBg

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssistenteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBg
                ) {
                    AssistenteAppNav()
                }
            }
        }
    }
}

@Composable
fun AssistenteAppNav() {
    val navController = rememberNavController()
    var userRole by remember { mutableStateOf(UserRole.GUEST) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    userRole = role
                    if (role == UserRole.ADMIN) {
                        navController.navigate("admin") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("guest") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }
        composable("admin") {
            AdminScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("guest") {
            GuestScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
