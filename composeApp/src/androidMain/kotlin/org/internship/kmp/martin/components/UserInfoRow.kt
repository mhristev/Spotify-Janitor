package org.internship.kmp.martin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun UserInfoRow(label: String, value: String, icon: ImageVector, isPassword: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label icon",
            modifier = Modifier.size(24.dp),
            tint = Color(0xFFBDBDBD)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = TextStyle(color = Color(0xFFBDBDBD), fontSize = 14.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (isPassword) {
                Text(
                    text = "•".repeat(value.length), // Display password as dots
                    style = TextStyle(color = Color.White, fontSize = 16.sp)
                )
            } else {
                Text(
                    text = value,
                    style = TextStyle(color = Color.White, fontSize = 16.sp)
                )
            }
        }
    }
}
