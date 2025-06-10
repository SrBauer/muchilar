package com.example.mochilar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mochilar.data.UserDatabase
import com.example.mochilar.screens.AppNavigation
import com.example.mochilar.ui.theme.MochilarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = UserDatabase.getDatabase(applicationContext)

        enableEdgeToEdge()
        setContent {
            MochilarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(paddingValues = innerPadding, database = database)
                }
            }
        }
    }
}
