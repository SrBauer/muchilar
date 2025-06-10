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
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelsScreen(navController: NavController, viewModel: TravelViewModel, userId: Int) {
    val context = LocalContext.current

    var destination by remember { mutableStateOf("") }
    var travelType by remember { mutableStateOf("Lazer") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val customGreen = Color(0xFF2A8056)


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
                        onClick = { travelType = type },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = customGreen,
                            unselectedColor = customGreen
                        )
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
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "Selecionar Data",
                        tint = customGreen
                    )
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
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "Selecionar Data",
                        tint = customGreen
                    )
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
                if (destination.isNotBlank() && startDate.isNotBlank() && endDate.isNotBlank() && budget.isNotBlank()) {

                    if (!isEndDateAfterStartDate(startDate, endDate)) {
                        Toast.makeText(context, "A data final deve ser posterior ou igual à data de início.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    try {
                        val budgetDouble = budget.toDoubleOrNull()
                        if (budgetDouble == null || budgetDouble < 0) {
                            Toast.makeText(context, "Digite um valor numérico válido e positivo para o orçamento.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val travel = Travel(
                            id = 0,
                            userId = userId,
                            destination = destination,
                            travelType = travelType,
                            startDate = startDate,
                            endDate = endDate,
                            budget = budgetDouble
                        )

                        viewModel.insertTravel(travel)
                        Toast.makeText(context, "Viagem cadastrada!", Toast.LENGTH_SHORT).show()

                        destination = ""
                        travelType = "Lazer"
                        startDate = ""
                        endDate = ""
                        budget = ""

                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Digite um valor numérico válido para o orçamento.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = customGreen,
                contentColor = Color.White
            )
        ) {
            Text("Cadastrar")
        }
    }
}