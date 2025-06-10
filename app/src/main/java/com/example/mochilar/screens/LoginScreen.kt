package com.example.mochilar.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mochilar.R
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.mochilar.data.UserDatabase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, paddingValues: PaddingValues, database: UserDatabase, onLoginSuccess: (Int) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    var loginError by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.mochilando),
            contentDescription = "Logo Mochilando",
            modifier = Modifier
                .size(260.dp)
                .padding(bottom = 24.dp)
        )

        Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = {email = it},
            label = {Text(emailError.ifEmpty { "E-mail" }, color = if (emailError.isNotEmpty()) Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = "",
                    tint = Color(0xFF2A8056)
                )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(500.dp)
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {password = it},
            label = {Text(passwordError.ifEmpty { "Senha" }, color = if (passwordError.isNotEmpty()) Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon(
                    Icons.Rounded.Lock,
                    contentDescription = "",
                    tint = Color(0xFF2A8056)
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                Icon(
                    imageVector = image,
                    contentDescription = "",
                    tint = Color(0xFF2A8056),
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(500.dp)
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = Color.Red)
        }

        Button(
            onClick = {
                emailError = if (email.isBlank()) "E-mail é obrigatório" else ""
                passwordError = if (password.isBlank()) "Senha é obrigatória" else ""
                if (emailError.isEmpty() && passwordError.isEmpty()) {
                    coroutineScope.launch {
                        val userDao = database.userDao()
                        val user = userDao.getUser(email, password)

                        if (user != null) {
                            onLoginSuccess(user.id)
                            navController.navigate("Início") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            loginError = "E-mail ou senha incorretos!"
                        }
                    }
                }
            },
            modifier = Modifier
                .width(400.dp)
                .padding(horizontal = 90.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2A8056),
                contentColor = Color.White
            )

        ) {
            Text(text = "Entrar")
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row {
            Text(text = "Não tem uma conta? ")

            Text(
                text = "Cadastre-se",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}
