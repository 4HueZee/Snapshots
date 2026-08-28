package com.example.battlebarge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PrivacySecuritySection() {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Email Verification Status
        ListItem(
            headlineContent = { Text("Account Status") },
            supportingContent = { 
                Text(
                    text = if (user?.isEmailVerified == true) "Email Verified" else "Email Not Verified",
                    color = if (user?.isEmailVerified == true) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
                )
            },
            leadingContent = { Icon(Icons.Default.Shield, contentDescription = null) }
        )

        // Password Options
        val isEmailProvider = user?.providerData?.any { it.providerId == "password" } == true
        if (isEmailProvider) {
            // Change Password (Direct)
            ListItem(
                headlineContent = { Text("Change Password") },
                supportingContent = { Text("Update your password directly") },
                leadingContent = { Icon(Icons.Default.LockReset, contentDescription = null) },
                modifier = Modifier.clickable {
                    showChangePasswordDialog = true
                }
            )

            // Reset Password (Email)
            ListItem(
                headlineContent = { Text("Reset via Email") },
                supportingContent = { Text("Send a recovery link to ${user?.email}") },
                leadingContent = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.clickable {
                    user?.email?.let { email ->
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                message = if (task.isSuccessful) {
                                    "Reset email sent to $email"
                                } else {
                                    "Failed to send reset email"
                                }
                            }
                    }
                }
            )
        }

        // Delete Account
        ListItem(
            headlineContent = { 
                Text("Delete Account", color = MaterialTheme.colorScheme.error) 
            },
            supportingContent = { Text("Permanently remove all your data") },
            leadingContent = { 
                Icon(
                    Icons.Default.DeleteForever, 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.error
                ) 
            },
            modifier = Modifier.clickable {
                showDeleteDialog = true
            }
        )

        message?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = if (it.contains("sent")) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Account?") },
                text = { Text("This action is permanent and cannot be undone. All your progress will be lost.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            user?.delete()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Activity reacts to Auth state
                                } else {
                                    message = "Please re-log in to delete account"
                                    showDeleteDialog = false
                                }
                            }
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showChangePasswordDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showChangePasswordDialog = false
                    newPassword = ""
                },
                title = { Text("Change Password") },
                text = {
                    Column {
                        Text("Enter your new password below.")
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        enabled = newPassword.length >= 6,
                        onClick = {
                            user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                                message = if (task.isSuccessful) {
                                    "Password updated successfully"
                                } else {
                                    "Failed to update password. Please re-log in."
                                }
                                showChangePasswordDialog = false
                                newPassword = ""
                            }
                        }
                    ) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { 
                        showChangePasswordDialog = false
                        newPassword = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
