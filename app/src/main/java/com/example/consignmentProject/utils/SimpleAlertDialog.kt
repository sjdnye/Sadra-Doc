package com.example.consignmentProject.utils

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SimpleAlertDialog(
    modifier: Modifier,
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit,
    dismissButton: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    confirmButton()
                }
            ) { Text(text = "OK") }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismissButton()
                }
            ) { Text(text = "Cancel") }
        },
        title = { Text(text = title) },
        text = { Text(text = description) }
    )
}