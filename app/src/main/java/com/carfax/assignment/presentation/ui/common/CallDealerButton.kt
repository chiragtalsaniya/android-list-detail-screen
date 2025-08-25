package com.carfax.assignment.presentation.ui.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.carfax.assignment.R

private fun sanitize(raw: String) = raw.trim().let {
    val plus = it.startsWith("+")
    val digits = it.filter(Char::isDigit)
    if (plus) "+$digits" else digits
}

enum class CallButtonStyle { Text, Filled }

@Composable
fun CallDealerButton(
    phone: String?,
    onDenied: () -> Unit = {},
    style: CallButtonStyle = CallButtonStyle.Text,   // default = text-style (for list)
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tel = remember(phone) { phone?.let(::sanitize) }
    var showConfirm by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && !tel.isNullOrBlank()) {
            context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$tel")))
        } else onDenied()
    }

    val startCall: () -> Unit = {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$tel")))
        } else {
            launcher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    when (style) {
        CallButtonStyle.Text -> {
            TextButton(
                modifier = modifier,
                enabled = !tel.isNullOrBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                onClick = { if (!tel.isNullOrBlank()) showConfirm = true }) {
                Text(
                    stringResource(R.string.call_dealer),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        CallButtonStyle.Filled -> {
            Button(
                modifier = modifier,
                enabled = !tel.isNullOrBlank(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                onClick = { if (!tel.isNullOrBlank()) showConfirm = true }) {
                Text(stringResource(R.string.call_dealer))
            }
        }
    }

    if (showConfirm && !tel.isNullOrBlank()) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text(stringResource(R.string.confirm_call_title)) },
            text = { Text(stringResource(R.string.confirm_call_message, tel)) },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    startCall()
                }) { Text(stringResource(R.string.call_action)) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            })
    }
}
