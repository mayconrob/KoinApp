package com.mayconrob.koinapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mayconrob.koinapp.data.local.CategoryEntity
import com.mayconrob.koinapp.model.TransactionType
import com.mayconrob.koinapp.ui.theme.ExpenseRed
import com.mayconrob.koinapp.ui.theme.IncomeGreen
import java.math.BigDecimal

@Composable
fun AddCategoryDialog(
    categoryToEdit: CategoryEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, type: TransactionType, budgetLimit: BigDecimal, colorHex: String) -> Unit
) {
    val colorOptions = listOf("#4CAF50", "#3F51B5", "#FF9800", "#E91E63", "#9C27B0", "#00BCD4", "#F44336")

    var name by remember { mutableStateOf(categoryToEdit?.name ?: "") }
    var selectedType by remember { mutableStateOf(categoryToEdit?.type ?: TransactionType.EXPENSE) }
    var budgetLimitText by remember {
        mutableStateOf(
            if (categoryToEdit != null && categoryToEdit.budgetLimit > BigDecimal.ZERO)
                categoryToEdit.budgetLimit.toPlainString()
            else ""
        )
    }
    var selectedColor by remember { mutableStateOf(categoryToEdit?.colorHex ?: colorOptions[0]) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isEditing = categoryToEdit != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing) "Editar Categoria" else "Nova Categoria",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column {
                // Tipo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedType == TransactionType.EXPENSE,
                        onClick = { selectedType = TransactionType.EXPENSE }
                    )
                    Text("Despesa", color = ExpenseRed)
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    RadioButton(
                        selected = selectedType == TransactionType.INCOME,
                        onClick = { selectedType = TransactionType.INCOME }
                    )
                    Text("Receita", color = IncomeGreen)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Nome da Categoria
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Categoria (ex: Pets, Assinaturas)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (selectedType == TransactionType.EXPENSE) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Teto de Orçamento
                    OutlinedTextField(
                        value = budgetLimitText,
                        onValueChange = { budgetLimitText = it },
                        label = { Text("Teto de Orçamento Mensal R$ (Opcional)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Escolha uma Cor:", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(6.dp))

                // Cores
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    colorOptions.forEach { hex ->
                        val color = try {
                            Color(android.graphics.Color.parseColor(hex))
                        } catch (e: Exception) {
                            Color.Gray
                        }
                        val isSelected = selectedColor.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                errorMessage?.let { err ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = err, color = ExpenseRed, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Informe o nome da categoria"
                    } else {
                        val cleanLimitText = budgetLimitText.replace(',', '.').trim()
                        val limit = try {
                            if (cleanLimitText.isNotEmpty()) BigDecimal(cleanLimitText) else BigDecimal.ZERO
                        } catch (e: Exception) {
                            BigDecimal.ZERO
                        }
                        onConfirm(name, selectedType, limit, selectedColor)
                        onDismiss()
                    }
                }
            ) {
                Text(if (isEditing) "Salvar" else "Cadastrar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
