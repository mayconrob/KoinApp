package com.mayconrob.koinapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayconrob.koinapp.data.local.TransactionEntity
import com.mayconrob.koinapp.data.local.TransactionWithCategory
import com.mayconrob.koinapp.data.repository.ITransacaoRepository
import com.mayconrob.koinapp.model.TransactionType
import com.mayconrob.koinapp.util.Formatters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ExtratoTransacoesViewModel @Inject constructor(
    private val transacaoRepository: ITransacaoRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    private val _tipoFiltroData = MutableStateFlow(TipoFiltroData.TODOS)
    val tipoFiltroData = _tipoFiltroData.asStateFlow()

    private val _dataInicio = MutableStateFlow<Long?>(null)
    val dataInicio = _dataInicio.asStateFlow()

    private val _dataFim = MutableStateFlow<Long?>(null)
    val dataFim = _dataFim.asStateFlow()

    private val _uiState = MutableStateFlow(ExtratoTransacoesUiState())
    val uiState: StateFlow<ExtratoTransacoesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val filtroDataFlow = combine(_tipoFiltroData, _dataInicio, _dataFim) { tipo, inicio, fim ->
                Triple(tipo, inicio, fim)
            }

            combine(
                transacaoRepository.all,
                _searchQuery,
                _selectedCategoryId,
                filtroDataFlow
            ) { transactions, query, filterCatId, filtroData ->
                val (tipoData, inicio, fim) = filtroData

                val transacoesFiltradas = transactions.filter { item ->
                    val timestamp = item.transaction.dateTimestamp

                    // 1. Filtro por Data (Sem considerar horário)
                    val matchesDate = when (tipoData) {
                        TipoFiltroData.TODOS -> true
                        TipoFiltroData.ULTIMOS_7_DIAS -> {
                            val (start7, end7) = Formatters.getLast7DaysRange()
                            timestamp in start7..end7
                        }
                        TipoFiltroData.PERIODO -> {
                            if (inicio != null && fim != null) {
                                val start = Formatters.getStartOfDay(inicio)
                                val end = Formatters.getEndOfDay(fim)
                                timestamp in start..end
                            } else true
                        }
                    }

                    // 2. Filtro por Busca de Texto
                    val matchesQuery = item.transaction.description.contains(query, ignoreCase = true) ||
                            item.category.name.contains(query, ignoreCase = true)

                    // 3. Filtro por Categoria Selecionada
                    val matchesCategory = filterCatId == null || item.transaction.categoryId == filterCatId

                    matchesDate && matchesQuery && matchesCategory
                }

                ExtratoTransacoesUiState(
                    transacoes = transacoesFiltradas,
                    buscaQuery = query,
                    categoriaFiltroId = filterCatId,
                    tipoFiltroData = tipoData,
                    dataInicioTimestamp = inicio,
                    dataFimTimestamp = fim,
                    estaCarregando = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryFilterChanged(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }

    fun onTipoFiltroDataChanged(tipo: TipoFiltroData) {
        _tipoFiltroData.value = tipo
    }

    fun onPeriodoDataChanged(dataInicio: Long?, dataFim: Long?) {
        _dataInicio.value = dataInicio
        _dataFim.value = dataFim
        _tipoFiltroData.value = TipoFiltroData.PERIODO
    }

    fun addTransaction(description: String, amount: BigDecimal, type: TransactionType, categoryId: Long) {
        viewModelScope.launch {
            val entity = TransactionEntity(
                description = description,
                amount = amount,
                type = type,
                categoryId = categoryId,
                dateTimestamp = System.currentTimeMillis()
            )
            transacaoRepository.insert(entity)
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transacaoRepository.update(transaction)
        }
    }

    fun deleteTransaction(item: TransactionWithCategory) {
        viewModelScope.launch {
            transacaoRepository.delete(item.transaction)
        }
    }
}
