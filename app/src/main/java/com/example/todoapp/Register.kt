package com.example.todoapp // Use your actual package name

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.example.todoapp.ads.BannerAdView
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RoyalBlue
import com.example.todoapp.ui.theme.ToDoAppTheme
import java.util.regex.Pattern

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                RegistrationScreen()
            }
        }
    }
}

// Composable for the Registration Screen
@Composable
fun RegistrationScreen() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Make status bar transparent
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(DarkBlue, RoyalBlue)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(bottom = 50.dp), // Add bottom padding for banner ad
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = true, enter = fadeIn(tween(1000, 500))) {
                Text(
                    text = "Create Account",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(visible = true, enter = fadeIn(tween(1000, 700))) {
                Text(
                    text = "Join us to manage your tasks",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(48.dp))

            // Email Field
            AnimatedVisibility(visible = true, enter = slideInHorizontally(tween(1000, 900)) { -it } + fadeIn(tween(1000, 900))) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.White.copy(alpha = 0.7f)) },
                    leadingIcon = { Icon(Icons.Default.Email, "Email", tint = Color.White) },
                    colors = outlinedTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            AnimatedVisibility(visible = true, enter = slideInHorizontally(tween(1000, 1100)) { it } + fadeIn(tween(1000, 1100))) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.White.copy(alpha = 0.7f)) },
                    leadingIcon = { Icon(Icons.Default.Lock, "Password", tint = Color.White) },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, "Toggle Visibility", tint = Color.White)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = outlinedTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Interactive Password Strength Indicator
            PasswordStrengthIndicator(password = password)
            Spacer(modifier = Modifier.height(16.dp))


            // Confirm Password Field
            val passwordsMatch = password == confirmPassword
            AnimatedVisibility(visible = true, enter = slideInHorizontally(tween(1000, 1300)) { -it } + fadeIn(tween(1000, 1300))) {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password", color = Color.White.copy(alpha = 0.7f)) },
                    leadingIcon = { Icon(Icons.Default.Lock, "Confirm Password", tint = if (passwordsMatch || confirmPassword.isEmpty()) Color.White else MaterialTheme.colorScheme.error) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = outlinedTextFieldColors(passwordsMatch || confirmPassword.isEmpty()),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Registration Button
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                        if (password == confirmPassword) {
                            isLoading = true
                            auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                        context.startActivity(Intent(context, MainActivity::class.java))
                                        (context as? Activity)?.finishAffinity()
                                    } else {
                                        Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Register", fontSize = 18.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Link to Login
            Text(
                text = "Already have an account? Login",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.clickable {
                    (context as? Activity)?.finish() // Go back to LoginActivity
                }
            )
        }
        
        // Banner Ad at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BannerAdView()
        }
    }
}

@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = remember(password) { calculateStrength(password) }
    val strengthColor = when (strength) {
        0 -> Color.DarkGray
        1 -> MaterialTheme.colorScheme.error // Weak
        2 -> Color(0xFFF57C00) // Medium (Orange)
        3 -> Color.Yellow // Strong
        4 -> Color(0xFF388E3C) // Very Strong (Green)
        else -> Color.DarkGray
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
        ) {
            AnimatedVisibility(
                visible = strength > 0,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = strength / 4f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(strengthColor)
                )
            }
        }
    }
}

fun calculateStrength(password: String): Int {
    if (password.isEmpty()) return 0
    var score = 0
    if (password.length >= 8) score++
    if (Pattern.compile("[a-z]").matcher(password).find() && Pattern.compile("[A-Z]").matcher(password).find()) score++
    if (Pattern.compile("[0-9]").matcher(password).find()) score++
    if (Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) score++
    return if (password.length < 8 && score > 1) 1 else score
}

@Composable
fun outlinedTextFieldColors(isErrorFree: Boolean = true): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = if (isErrorFree) Color.White else MaterialTheme.colorScheme.error,
        unfocusedBorderColor = if (isErrorFree) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.error,
        cursorColor = Color.White
    )
}