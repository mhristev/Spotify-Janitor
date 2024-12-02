package org.internship.kmp.martin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import org.internship.kmp.martin.core.presentation.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TestView(accessToken: String, expiresIn: Int, navController: NavController) {
    val viewModel: LoginViewModel = koinViewModel()
LaunchedEffect(key1 = accessToken, key2 = expiresIn) {
    viewModel.login(accessToken, expiresIn)
    navController.navigate("home_screen")
}
    fun login(accessToken: String, expiresIn: Int) {
        viewModel.login(accessToken, expiresIn)

    }
}