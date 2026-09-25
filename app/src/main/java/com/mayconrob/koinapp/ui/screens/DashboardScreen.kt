package com.mayconrob.koinapp.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mayconrob.koinapp.ui.components.CategoryBudgetCard
import com.mayconrob.koinapp.ui.components.StatCard
import com.mayconrob.koinapp.ui.components.TransactionItem
import com.mayconrob.koinapp.ui.theme.AccentIndigo
import com.mayconrob.koinapp.ui.theme.ExpenseRed
import com.mayconrob.koinapp.ui.theme.IncomeGreen
import com.mayconrob.koinapp.ui.viewmodel.PainelFinanceiroUiState

@Composable
fun DashboardScreen(
    state: PainelFinanceiroUiState,
    onAddTransactionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = AccentIndigo,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nova Transação")
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
                Text(
                    text = "Visão Geral",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Card Saldo Atual
                StatCard(
                    title = "Saldo Total",
                    amount = state.resumo.currentBalance,
                    icon = Icons.Default.AccountBalanceWallet,
                    iconTint = AccentIndigo
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Cards Lado a Lado: Entradas vs Saídas
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard(
                        title = "Receitas",
                        amount = state.resumo.totalIncome,
                        icon = Icons.Default.ArrowUpward,
                        iconTint = IncomeGreen,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(
                        title = "Despesas",
                        amount = state.resumo.totalExpenses,
                        icon = Icons.Default.ArrowDownward,
                        iconTint = ExpenseRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Seção Orçamentos por Categoria
                if (state.consumosOrcamento.isNotEmpty()) {
                    Text(
                        text = "Tetos de Orçamento",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
    }
}
