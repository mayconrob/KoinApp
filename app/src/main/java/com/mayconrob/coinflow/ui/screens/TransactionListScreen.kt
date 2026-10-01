package com.mayconrob.coinflow.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mayconrob.coinflow.domain.model.Category
import com.mayconrob.coinflow.domain.model.TransactionWithCategory
import com.mayconrob.coinflow.ui.components.DateRangePickerDialog
import com.mayconrob.coinflow.ui.components.TransactionItem
import com.mayconrob.coinflow.ui.viewmodel.ExtratoTransacoesUiState
import com.mayconrob.coinflow.ui.viewmodel.enums.TipoFiltroData
import com.mayconrob.coinflow.ui.viewmodel.enums.TipoFiltroTransacao
import com.mayconrob.coinflow.common.Formatters
import com.mayconrob.coinflow.ui.theme.ExpenseRed

@Composable
fun TransactionListScreen(
    state: ExtratoTransacoesUiState,
    categories: List<Category>,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryFilterChanged: (Long?) -> Unit,
    onTipoFiltroDataChanged: (TipoFiltroData) -> Unit,
    onTipoFiltroTransacaoChanged: (TipoFiltroTransacao) -> Unit,
    onPeriodoDataChanged: (Long?, Long?) -> Unit,
    onEditTransactionClick: (TransactionWithCategory) -> Unit,
    onDeleteTransactionClick: (TransactionWithCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDateRangePicker by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<TransactionWithCategory?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Extrato Financeiro",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Campo de Busca por Descrição / Categoria
        OutlinedTextField(
            value = state.buscaQuery,
            onValueChange = onSearchQueryChanged,
            label = { Text("Buscar transação...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Chips de Filtro por Período de Data
        LazyRow {
            item {
                FilterChip(
                    selected = state.tipoFiltroData == TipoFiltroData.HOJE,
                    onClick = { onTipoFiltroDataChanged(TipoFiltroData.HOJE) },
                    label = { Text("Hoje") },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            item {
                FilterChip(
                    selected = state.tipoFiltroData == TipoFiltroData.ULTIMOS_7_DIAS,
                    onClick = { onTipoFiltroDataChanged(TipoFiltroData.ULTIMOS_7_DIAS) },
                    label = { Text("Últimos 7 dias") },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            item {
                val isPeriodoActive = state.tipoFiltroData == TipoFiltroData.PERIODO
                val periodoLabel = if (isPeriodoActive && state.dataInicioTimestamp != null && state.dataFimTimestamp != null) {
                    "${Formatters.formatShortDate(state.dataInicioTimestamp)} - ${Formatters.formatShortDate(state.dataFimTimestamp)}"
                } else {
                    "Período por Data"
                }

                FilterChip(
                    selected = isPeriodoActive,
                    onClick = { showDateRangePicker = true },
                    leadingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = null) },
                    label = { Text(periodoLabel) },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Chips de Filtro por Tipo de Transação (Todas / Receitas / Despesas)
        LazyRow {
            item {
                FilterChip(
                    selected = state.tipoFiltroTransacao == TipoFiltroTransacao.TODAS,
                    onClick = { onTipoFiltroTransacaoChanged(TipoFiltroTransacao.TODAS) },
                    label = { Text("Todas as Transações") },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            item {
                FilterChip(
                    selected = state.tipoFiltroTransacao == TipoFiltroTransacao.RECEITAS,
                    onClick = { onTipoFiltroTransacaoChanged(TipoFiltroTransacao.RECEITAS) },
                    label = { Text("Receitas") },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            item {
                FilterChip(
                    selected = state.tipoFiltroTransacao == TipoFiltroTransacao.DESPESAS,
                    onClick = { onTipoFiltroTransacaoChanged(TipoFiltroTransacao.DESPESAS) },
                    label = { Text("Despesas") },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Chips de Filtro por Categoria (Cumulativo)
        LazyRow {
            item {
                FilterChip(
                    selected = state.categoriaFiltroIds.isEmpty(),
                    onClick = { onCategoryFilterChanged(null) },
                    label = { Text("Todas as Categorias") },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            items(categories) { cat ->
                FilterChip(
                    selected = cat.id in state.categoriaFiltroIds,
                    onClick = { onCategoryFilterChanged(cat.id) },
                    label = { Text(cat.name) },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista de Transações
        if (state.transacoes.isEmpty()) {
            Text(
                text = "Nenhuma transação encontrada para os filtros selecionados.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 24.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.transacoes) { item ->
                    TransactionItem(
                        item = item,
                        onEditClick = onEditTransactionClick,
                        onDeleteClick = { transactionToDelete = it }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showDateRangePicker) {
        DateRangePickerDialog(
            initialStartTimestamp = state.dataInicioTimestamp,
            initialEndTimestamp = state.dataFimTimestamp,
            onDismiss = { showDateRangePicker = false },
            onConfirm = { start, end ->
                onPeriodoDataChanged(start, end)
                showDateRangePicker = false
            }
        )
    }

    if (transactionToDelete != null) {
        val target = transactionToDelete!!
        val desc = target.transaction.description.ifBlank { target.category.name }
        val amountStr = Formatters.formatCurrency(target.transaction.amount)

        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = {
                Text(
                    text = "Excluir Transação",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Tem certeza que deseja excluir a transação \"$desc\" no valor de $amountStr?",
                    modifier = Modifier.semantics {
                        contentDescription = "Confirmação de exclusão da transação $desc no valor de $amountStr"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTransactionClick(target)
                        transactionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ExpenseRed
                    ),
                    modifier = Modifier.semantics {
                        contentDescription = "Botão confirmar exclusão da transação $desc"
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { transactionToDelete = null },
                    modifier = Modifier.semantics {
                        contentDescription = "Botão cancelar exclusão da transação $desc"
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
