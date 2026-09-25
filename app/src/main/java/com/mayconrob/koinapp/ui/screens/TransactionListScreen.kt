package com.mayconrob.koinapp.ui.screens

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mayconrob.koinapp.data.local.CategoryEntity
import com.mayconrob.koinapp.data.local.TransactionWithCategory
import com.mayconrob.koinapp.ui.components.DateRangePickerDialog
import com.mayconrob.koinapp.ui.components.TransactionItem
import com.mayconrob.koinapp.ui.viewmodel.ExtratoTransacoesUiState
import com.mayconrob.koinapp.ui.viewmodel.TipoFiltroData
import com.mayconrob.koinapp.util.Formatters

@Composable
fun TransactionListScreen(
    state: ExtratoTransacoesUiState,
    categories: List<CategoryEntity>,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryFilterChanged: (Long?) -> Unit,
    onTipoFiltroDataChanged: (TipoFiltroData) -> Unit,
    onPeriodoDataChanged: (Long?, Long?) -> Unit,
    onEditTransactionClick: (TransactionWithCategory) -> Unit,
    onDeleteTransactionClick: (TransactionWithCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDateRangePicker by remember { mutableStateOf(false) }

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
                    selected = state.tipoFiltroData == TipoFiltroData.TODOS,
                    onClick = { onTipoFiltroDataChanged(TipoFiltroData.TODOS) },
                    label = { Text("Todas as Datas") },
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

        // Chips de Filtro por Categoria
        LazyRow {
            item {
                FilterChip(
                    selected = state.categoriaFiltroId == null,
                    onClick = { onCategoryFilterChanged(null) },
                    label = { Text("Todas as Categorias") },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            items(categories) { cat ->
                FilterChip(
                    selected = state.categoriaFiltroId == cat.id,
                    onClick = { onCategoryFilterChanged(if (state.categoriaFiltroId == cat.id) null else cat.id) },
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
                        onDeleteClick = onDeleteTransactionClick
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
}
