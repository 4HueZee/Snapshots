package com.example.battlebarge

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object SupportConfig {
    const val SUPPORT_EMAIL = "sprueguild@gmail.com.au"
}

@Composable
fun HelpSupportSection() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Contact Support via Email
        ListItem(
            headlineContent = { Text("Contact Support") },
            supportingContent = { Text("Send us an email at ${SupportConfig.SUPPORT_EMAIL}") },
            leadingContent = { Icon(Icons.Default.Email, contentDescription = null) },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(20.dp)) },
            modifier = Modifier.clickable {
                sendSupportEmail(context)
            }
        )

        // FAQ Placeholder
        ListItem(
            headlineContent = { Text("Community Guidelines & Safety") },
            supportingContent = { Text("Guidelines on personal info and safe interaction") },
            leadingContent = { Icon(Icons.AutoMirrored.Filled.HelpCenter, contentDescription = null) },
            modifier = Modifier.clickable {
                // Future: Open safety web page or dialog
            }
        )

        // Basic Safety Notice in UI
        Surface(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Safety Tip:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Never share your real name, address, or password with other users. Our team will never ask for your login credentials.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Version Info
        Text(
            text = "App Version: 1.0.0",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

private fun sendSupportEmail(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(SupportConfig.SUPPORT_EMAIL))
        putExtra(Intent.EXTRA_SUBJECT, "BattleBarge Support Request")
        putExtra(Intent.EXTRA_TEXT, "Hello Support Team,\n\nI need help with...")
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    } catch (_: Exception) {
        // Silently fail if no email client is installed
    }
}
