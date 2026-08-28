package com.example.battlebarge

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    var isSignUpMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "BATTLE BARGE",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (isSignUpMode) "Create your account to start your journey" else "Sign in to manage your collection",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = isSignUpMode && password.isNotEmpty() && password.length < 6,
            supportingText = {
                if (isSignUpMode && password.isNotEmpty() && password.length < 6) {
                    Text(
                        text = "Password must be at least 6 characters",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (isSignUpMode) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmPassword.isNotEmpty() && confirmPassword != password,
                supportingText = {
                    if (confirmPassword.isNotEmpty() && confirmPassword != password) {
                        Text(
                            text = "Passwords do not match",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isSignUpMode) {
                    when {
                        email.isEmpty() -> Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                        password.isEmpty() -> Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                        password.length < 6 -> Toast.makeText(context, "Password too short", Toast.LENGTH_SHORT).show()
                        password != confirmPassword -> Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        else -> performSignUp(email, password, context)
                    }
                } else {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        performLogin(email, password, context)
                    } else {
                        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(if (isSignUpMode) "Create Account" else "Log In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isSignUpMode) "Already have an account? " else "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(
                onClick = { isSignUpMode = !isSignUpMode },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = if (isSignUpMode) "Log in" else "Sign up",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "OR",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Official Google Button implementation
        GoogleSignInButton(
            onClick = {
                coroutineScope.launch {
                    triggerGoogleSignIn(context)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                performAnonymousSignIn(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Continue as Guest",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Function handling the official Credential Manager request
private suspend fun triggerGoogleSignIn(context: Context) {
    val credentialManager = CredentialManager.create(context)

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts = false)
        .setServerClientId(context.getString(R.string.google_web_client_id))
        .setAutoSelectEnabled(autoSelectEnabled = false) // Disable auto-select to force the picker for testing
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )

        val credential = result.credential
        if ((credential is CustomCredential) && (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            Log.d("LoginScreen", "Google Sign-In Success. Token: $idToken")
            
            // Link or Sign in with Firebase
            val firebaseAuth = FirebaseAuth.getInstance()
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val currentUser = firebaseAuth.currentUser

            if (currentUser != null && currentUser.isAnonymous) {
                currentUser.linkWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("LoginScreen", "Firebase Google Link Success")
                            Toast.makeText(context, "Account Linked Successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("LoginScreen", "Firebase Google Link Failed", task.exception)
                            // Fallback to sign in if link fails (e.g. account already exists)
                            firebaseAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { signInTask ->
                                    if (signInTask.isSuccessful) {
                                        Toast.makeText(context, "Sign-in Successful!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Link and Sign-in Failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
            } else {
                firebaseAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("LoginScreen", "Firebase Google Sign-In Success")
                            Toast.makeText(context, "Sign-in Successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("LoginScreen", "Firebase Google Sign-In Failed", task.exception)
                            Toast.makeText(context, "Firebase Authentication Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    } catch (e: Exception) {
        Log.e("LoginScreen", "Google Sign-In Error", e)
        val errorMessage = when (e) {
            is androidx.credentials.exceptions.GetCredentialCancellationException -> "Sign-in cancelled"
            is androidx.credentials.exceptions.NoCredentialException -> "No accounts found on device"
            else -> "Sign-in failed: ${e.message}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}

private fun performLogin(email: String, password: String, context: Context) {
    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
        return
    }

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("LoginScreen", "Login Failed", task.exception)
                Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
}

private fun performSignUp(email: String, password: String, context: Context) {
    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
        return
    }

    if (password.length < 6) {
        Toast.makeText(context, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
        return
    }

    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    if (currentUser != null && currentUser.isAnonymous) {
        val credential = EmailAuthProvider.getCredential(email, password)
        currentUser.linkWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Account Created & Linked Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("LoginScreen", "Sign-Up/Link Failed", task.exception)
                    Toast.makeText(context, "Sign-Up Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    } else {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    kotlinx.coroutines.MainScope().launch {
                        UserRepository.initializeThemedProfile()
                        Toast.makeText(context, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginScreen", "Sign-Up Failed", task.exception)
                    Toast.makeText(context, "Sign-Up Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}

private fun performAnonymousSignIn(context: Context) {
    val auth = FirebaseAuth.getInstance()
    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Initialize themed profile in background
                kotlinx.coroutines.MainScope().launch {
                    UserRepository.initializeThemedProfile()
                    Toast.makeText(context, "Welcome, Operative!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("LoginScreen", "Anonymous Sign-In Failed", task.exception)
                Toast.makeText(context, "Guest Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
}
