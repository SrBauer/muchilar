package com.example.mochilar.screens

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoteiroIAScreen(roteiro: String, onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roteiro com IA") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Sugestão de roteiro:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(4.dp)
            ) {
                Text(
                    text = roteiro,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}