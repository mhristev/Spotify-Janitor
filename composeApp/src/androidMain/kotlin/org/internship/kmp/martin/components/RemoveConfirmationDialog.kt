package org.internship.kmp.martin.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.domain.Track

@Composable
fun RemoveConfirmationDialog(
    track: Track?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (track != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirm Deletion", color = Color.White) },
            text = { Text("Are you sure you want to remove this track?", color = Color.White) },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Remove", color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt()))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt()))
                }
            },
            backgroundColor = Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt())
        )
    }
}