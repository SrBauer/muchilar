package com.example.mochilar.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mochilar.data.Travel
import com.example.mochilar.utils.isEndDateAfterStartDate
import com.example.mochilar.viewmodel.TravelViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTravelScreen(
    navController: NavController,
    viewModel: TravelViewModel,
    travelId: Int
) {
    val context = LocalContext.current
    var travel by remember { mutableStateOf<Travel?>(null) }

    var destination by remember { mutableStateOf("") }
    var travelType by remember { mutableStateOf("Lazer") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    val startDatePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth -> startDate = "$dayOfMonth/${month + 1}/$year" },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth -> endDate = "$dayOfMonth/${month + 1}/$year" },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(travelId) {
        travel = viewModel.getTravelById(travelId)
        travel?.let {
            destination = it.destination
            travelType = it.travelType
            startDate = it.startDate
            endDate = it.endDate
            budget = it.budget.toString()
        }
    }

    if (travel != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tipo de Viagem:")
            Row {
                listOf("Lazer", "Negócio").forEach { type ->
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = travelType == type,
                            onClick = { travelType = type }
                        )
                        Text(text = type, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            OutlinedTextField(
                value = startDate,
                onValueChange = {},
                label = { Text("Data de Início") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { startDatePicker.show() }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar Data")
                    }
                }
            )

            OutlinedTextField(
                value = endDate,
                onValueChange = {},
                label = { Text("Data Final") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { endDatePicker.show() }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar Data")
                    }
                }
            )

            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("Orçamento ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val budgetDouble = budget.toDoubleOrNull()
                    if (budgetDouble == null || destination.isBlank() || startDate.isBlank() || endDate.isBlank()) {
                        Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (!isEndDateAfterStartDate(startDate, endDate)) {
                        Toast.makeText(context, "A data final deve ser depois da inicial.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val updatedTravel = Travel(
                        id = travelId,
                        userId = travel!!.userId,
                        destination = destination,
                        travelType = travelType,
                        startDate = startDate,
                        endDate = endDate,
                        budget = budgetDouble
                    )

                    viewModel.updateTravel(updatedTravel)
                    Toast.makeText(context, "Viagem atualizada!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
