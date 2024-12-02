package org.internship.kmp.martin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.internship.kmp.martin.core.presentation.AuthViewModel
import org.internship.kmp.martin.views.AuthView
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigator(navController: NavHostController,) {
    val viewModel: AuthViewModel = koinViewModel()

    NavHost(navController, startDestination = if (viewModel.isUserLoggedIn()) "home_screen" else "auth_screen") {
        composable("auth_screen") {
            AuthView(navController)
        }
        composable("home_screen") {
            App(navController)
        }
    }


}