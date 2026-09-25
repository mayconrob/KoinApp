package com.mayconrob.koinapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mayconrob.koinapp.ui.theme.ExpenseRed
import com.mayconrob.koinapp.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    initialStartTimestamp: Long? = null,
    initialEndTimestamp: Long? = null,
    onDismiss: () -> Unit,
    onConfirm: (startTimestamp: Long, endTimestamp: Long) -> Unit
) {
    var selectedStartMillis by remember { mutableStateOf(initialStartTimestamp ?: System.currentTimeMillis()) }
    var selectedEndMillis by remember { mutableStateOf(initialEndTimestamp ?: System.currentTimeMillis()) }

    var showStartCalendarPicker by remember { mutableStateOf(false) }
    var showEndCalendarPicker by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filtrar por Período de Datas",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column {
                Text(
                    text = "Selecione a Data Início e a Data Fim usando o calendário interativo (Dia, Mês e Ano):",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Escolha da Data Início (Somente Seleção por Clique)
                OutlinedTextField(
                    value = Formatters.formatShortDate(selectedStartMillis),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Data Início (Dia/Mês/Ano)") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Escolher Data Início")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartCalendarPicker = true },
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo de Escolha da Data Fim (Somente Seleção por Clique)
                OutlinedTextField(
                    value = Formatters.formatShortDate(selectedEndMillis),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Data Fim (Dia/Mês/Ano)") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Escolher Data Fim")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndCalendarPicker = true },
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                    )
                )

                errorMessage?.let { err ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = err, color = ExpenseRed, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val startOfDay = Formatters.getStartOfDay(selectedStartMillis)
                    val endOfDay = Formatters.getEndOfDay(selectedEndMillis)

                    if (startOfDay > endOfDay) {
                        errorMessage = "A Data Início deve ser anterior ou igual à Data Fim"
                    } else {
                        onConfirm(startOfDay, endOfDay)
                        onDismiss()
                    }
                }
            ) {
                Text("Aplicar Filtro")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )

    // Modal do Calendário Interativo para Data Início
    if (showStartCalendarPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedStartMillis
        )
        DatePickerDialog(
            onDismissRequest = { showStartCalendarPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedStartMillis = it }
                        showStartCalendarPicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartCalendarPicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Modal do Calendário Interativo para Data Fim
    if (showEndCalendarPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedEndMillis
        )
        DatePickerDialog(
            onDismissRequest = { showEndCalendarPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedEndMillis = it }
                        showEndCalendarPicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndCalendarPicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
