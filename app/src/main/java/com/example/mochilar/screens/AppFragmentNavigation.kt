package com.example.mochilar.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.mochilar.data.TravelRepository
import com.example.mochilar.data.UserDatabase
import com.example.mochilar.data.model.NavigationItems
import com.example.mochilar.viewmodel.TravelViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(paddingValues: PaddingValues, database: UserDatabase) {
    val navController = rememberNavController()
    var isAuthenticated by rememberSaveable { mutableStateOf(false) }

    val items = listOf(
        NavigationItems("Início", Icons.Filled.Home),
        NavigationItems("Nova viagem", Icons.Filled.Flight),
        NavigationItems("Sobre", Icons.Filled.Info),
    )

    val GreenPrimary = Color(0xFF2A8056)

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val screenTitle = when (selectedItemIndex) {
        0 -> "Início"
        1 -> "Nova viagem"
        2 -> "Sobre"
        else -> "Início"
    }

    var userId by rememberSaveable { mutableStateOf<Int?>(null) }

    val travelRepository = TravelRepository(database.travelDao())
    val travelViewModel = TravelViewModel(travelRepository)

    Scaffold(
        topBar = {
            if (isAuthenticated) {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                    title = { Text(text = screenTitle) },
                    actions = {
                        IconButton(onClick = {
                            isAuthenticated = false
                            userId = null
                            navController.navigate("login") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Sair",
                                tint = Color.Black
                            )
                        }
                    }
                )

            }
        },
        bottomBar = {
            if (isAuthenticated) {
                NavigationBar(
                    items = items,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { index, route ->
                        selectedItemIndex = index
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = "login", modifier = Modifier.padding(padding)) {
            composable("login") {
                LoginScreen(navController, paddingValues, database) { id ->
                    isAuthenticated = true
                    userId = id
                    navController.navigate("Início") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            composable("register") {
                RegisterScreen(navController, paddingValues, database) {
                    isAuthenticated = true
                    navController.navigate("Início") {
                        popUpTo("register") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            composable("Início") {
                userId?.let { HomeScreen(navController, viewModel = travelViewModel, userId = it) }
            }
            composable("Nova viagem") {
                userId?.let { TravelsScreen(navController, viewModel = travelViewModel, userId = it) }
            }
            composable("Sobre") { AboutScreen() }
            composable("editTravel/{travelId}") { backStackEntry ->
                val travelId = backStackEntry.arguments?.getString("travelId")?.toIntOrNull() ?: return@composable
                EditTravelScreen(navController, viewModel = travelViewModel, travelId = travelId)
            }
            composable("roteiroIA/{roteiro}") { backStackEntry ->
                val roteiro = backStackEntry.arguments?.getString("roteiro") ?: ""
                RoteiroIAScreen(roteiro = roteiro) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun NavigationBar(
    items: List<NavigationItems>,
    selectedItemIndex: Int,
    onItemSelected: (Int, String) -> Unit
) {
    androidx.compose.material3.NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 2.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = { onItemSelected(index, item.name) },
                icon = {
                    Icon(
                        imageVector = item.logo,
                        contentDescription = item.name,
                        tint = if (selectedItemIndex == index) MaterialTheme.colorScheme.primary else Color.Black,
                        modifier = Modifier.size(if (selectedItemIndex == index) 35.dp else 30.dp)
                    )
                }
            )
        }
    }
}