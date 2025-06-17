package com.example.mochilar.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoteiroIAScreen(
    roteiro: String,
    onBack: () -> Unit,
    onNovaSugestao: (String) -> Unit
) {
    var sugestaoUsuario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Roteiro Sugerido",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = roteiro,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = sugestaoUsuario,
            onValueChange = { sugestaoUsuario = it },
            label = { Text("Escreva sua sugestão para a IA") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = onBack) {
                Text("Voltar")
            }

            Button(onClick = {
                if (sugestaoUsuario.isNotBlank()) {
                    onNovaSugestao(sugestaoUsuario)
                }
            }) {
                Text("Enviar sugestão personalizada")
            }
        }
    }
}
