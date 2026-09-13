package com.focusflow.ai.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusflow.ai.ui.theme.*

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuralCoreGradient)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Initialize Profile",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = TextCyberWhite,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Connect your workflow to the FocusFlow Neural Matrix",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextCyberMuted),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkSurface.copy(alpha = 0.9f)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, CyberCardGlowBorder, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; localError = null },
                    label = { Text("Your Name", color = TextCyberMuted) },
                    placeholder = { Text("e.g. Kasun Silva", color = TextCyberSubtle) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextCyberWhite,
                        unfocusedTextColor = TextCyberWhite,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; localError = null },
                    label = { Text("Email Address", color = TextCyberMuted) },
                    placeholder = { Text("name@example.com", color = TextCyberSubtle) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonViolet) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextCyberWhite,
                        unfocusedTextColor = TextCyberWhite,
                        focusedBorderColor = NeonViolet,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; localError = null },
                    label = { Text("Password", color = TextCyberMuted) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AccentCoral) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextCyberWhite,
                        unfocusedTextColor = TextCyberWhite,
                        focusedBorderColor = AccentCoral,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; localError = null },
                    label = { Text("Confirm Password", color = TextCyberMuted) },
                    leadingIcon = { Icon(Icons.Default.LockClock, contentDescription = null, tint = AccentCoral) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextCyberWhite,
                        unfocusedTextColor = TextCyberWhite,
                        focusedBorderColor = AccentCoral,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                val displayError = localError ?: authState.errorMessage
                if (displayError != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = displayError,
                        color = NeonRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        if (name.isBlank() || email.isBlank() || password.isBlank()) {
                            localError = "Please fill in all fields"
                        } else if (password != confirmPassword) {
                            localError = "Passwords do not match"
                        } else {
                            isSubmitting = true
                            authViewModel.signUp(name, email, password) { success ->
                                isSubmitting = false
                                if (success) onSignUpSuccess()
                            }
                        }
                    },
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .border(1.5.dp, CyberNeonGradient, RoundedCornerShape(16.dp))
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Create Account & Flow",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Existing operator?", color = TextCyberMuted, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign In",
                color = NeonCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}