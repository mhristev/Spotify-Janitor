package org.internship.kmp.martin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun UserProfileView(accessToken: String) {
    Text(accessToken)
}
