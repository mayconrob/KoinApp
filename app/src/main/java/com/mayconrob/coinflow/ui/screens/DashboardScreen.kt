package com.mayconrob.coinflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mayconrob.coinflow.ui.components.CategoryBudgetCard
import com.mayconrob.coinflow.ui.components.StatCard
import com.mayconrob.coinflow.ui.components.TransactionItem
import com.mayconrob.coinflow.ui.theme.AccentIndigo
import com.mayconrob.coinflow.ui.theme.ExpenseRed
import com.mayconrob.coinflow.ui.theme.IncomeGreen
import com.mayconrob.coinflow.ui.viewmodel.PainelFinanceiroUiState

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mayconrob.coinflow.ui.components.MonthYearPickerDialog

@Composable
fun DashboardScreen(
    state: PainelFinanceiroUiState,
    onAddTransactionClick: () -> Unit,
    onToggleVisibility: () -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthYearSelected: (year: Int, month: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMonthYearPicker by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = AccentIndigo,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.semantics {
                    contentDescription = "Adicionar nova transação"
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar nova transação")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Cabeçalho com Título e Seletor Mês/Ano
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Visão Geral",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    // Seletor Mês/Ano com Navegação
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = onPreviousMonth,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Mês Anterior",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = state.currentMonthLabel,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .clickable { showMonthYearPicker = true }
                                    .padding(horizontal = 4.dp)
                            )
                            IconButton(
                                onClick = onNextMonth,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Próximo Mês",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card Saldo Atual
                StatCard(
                    title = "Saldo Total",
                    amount = state.resumo.currentBalance,
                    icon = Icons.Default.AccountBalanceWallet,
                    iconTint = AccentIndigo,
                    showIcon = true,
                    isValuesVisible = state.isValuesVisible,
                    trailingContent = {
                        IconButton(
                            onClick = onToggleVisibility,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isValuesVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (state.isValuesVisible) "Ocultar valores" else "Mostrar valores",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Cards Lado a Lado: Entradas vs Saídas
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard(
                        title = "Entradas",
                        amount = state.resumo.totalIncome,
                        icon = Icons.Default.ArrowUpward,
                        iconTint = IncomeGreen,
                        dotColor = IncomeGreen,
                        isValuesVisible = state.isValuesVisible,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(
                        title = "Saídas",
                        amount = state.resumo.totalExpenses,
                        icon = Icons.Default.ArrowDownward,
                        iconTint = ExpenseRed,
                        dotColor = ExpenseRed,
                        isValuesVisible = state.isValuesVisible,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Seção Orçamentos por Categoria
                if (state.consumosOrcamento.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tetos de Orçamento",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = state.currentMonthLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            items(state.consumosOrcamento) { consumption ->
                CategoryBudgetCard(consumption = consumption)
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Últimas Transações",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.ultimasTransacoes.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma transação lançada ainda. Clique no botão + para adicionar!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            } else {
                items(state.ultimasTransacoes) { item ->
                    TransactionItem(
                        item = item
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        if (showMonthYearPicker) {
            MonthYearPickerDialog(
                initialYear = state.selectedYear,
                initialMonth = state.selectedMonth,
                onDismiss = { showMonthYearPicker = false },
                onConfirm = { year, month ->
                    onMonthYearSelected(year, month)
                    showMonthYearPicker = false
                }
            )
        }
    }
}
