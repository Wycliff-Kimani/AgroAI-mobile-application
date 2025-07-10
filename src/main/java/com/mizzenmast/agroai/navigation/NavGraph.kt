package com.mizzenmast.agroai.navigation

// navigation/NavGraph.kt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mizzenmast.agroai.ui.screens.DashboardScreen
import com.mizzenmast.agroai.ui.screens.ForgotPasswordScreen
import com.mizzenmast.agroai.ui.screens.LoginScreen
import com.mizzenmast.agroai.ui.screens.ProfileScreen
import com.mizzenmast.agroai.ui.screens.RegisterScreen
import com.mizzenmast.agroai.ui.screens.ResultsScreen
import com.mizzenmast.agroai.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "dashboard" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                onLoginSuccess = { navController.navigate("dashboard") },
                authViewModel = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = { navController.navigate("dashboard") },
                authViewModel = authViewModel
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToResults = { navController.navigate("results") }
            )
        }

        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable("results") {
            ResultsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}