package org.internship.kmp.martin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.core.presentation.AuthViewModel
import org.internship.kmp.martin.views.AuthView
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavigator(navController: NavHostController) {
    val authRepository: AuthRepository = koinInject()
    val isUserLoggedIn by authRepository.isUserLoggedIn().collectAsState()

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigate("home_screen")
        } else {
            navController.navigate("auth_screen")
        }
    }

    NavHost(navController, startDestination = "auth_screen") {
        composable("auth_screen") {
            AuthView(navController)
        }
        composable("home_screen") {
            App(navController)
        }
    }

}