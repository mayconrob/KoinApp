package com.mayconrob.coinflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayconrob.coinflow.domain.model.Transaction
import com.mayconrob.coinflow.domain.model.TransactionWithCategory
import com.mayconrob.coinflow.domain.repository.ITransacaoRepository
import com.mayconrob.coinflow.domain.enums.TransactionType
import com.mayconrob.coinflow.common.Formatters
import com.mayconrob.coinflow.ui.viewmodel.enums.TipoFiltroData
import com.mayconrob.coinflow.ui.viewmodel.enums.TipoFiltroTransacao
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

    private val _selectedCategoryIds = MutableStateFlow<Set<Long>>(emptySet())

    private val _tipoFiltroData = MutableStateFlow(TipoFiltroData.HOJE)
    val tipoFiltroData = _tipoFiltroData.asStateFlow()

    private val _tipoFiltroTransacao = MutableStateFlow(TipoFiltroTransacao.TODAS)
    val tipoFiltroTransacao = _tipoFiltroTransacao.asStateFlow()

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
                _selectedCategoryIds,
                _tipoFiltroTransacao,
                filtroDataFlow
            ) { transactions, query, filterCatIds, tipoTransacao, filtroData ->
                val (tipoData, inicio, fim) = filtroData

                val transacoesFiltradas = transactions.filter { item ->
                    val timestamp = item.transaction.dateTimestamp

                    // 1. Filtro por Data (Sem considerar horário)
                    val matchesDate = when (tipoData) {
                        TipoFiltroData.HOJE -> {
                            val (startToday, endToday) = Formatters.getTodayRange()
                            timestamp in startToday..endToday
                        }
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
                    val matchesQuery = if (query.isNotBlank()) {
                        if (item.transaction.description.isBlank()) {
                            false
                        } else {
                            item.run { transaction.description.contains(query, ignoreCase = true) }
                        }
                    } else {
                        true
                    }

                    // 3. Filtro por Categorias Selecionadas (Cumulativo)
                    val matchesCategory = filterCatIds.isEmpty() || item.transaction.categoryId in filterCatIds

                    // 4. Filtro por Tipo de Transação (Todas / Receitas / Despesas)
                    val matchesType = when (tipoTransacao) {
                        TipoFiltroTransacao.TODAS -> true
                        TipoFiltroTransacao.RECEITAS -> item.transaction.type == TransactionType.INCOME
                        TipoFiltroTransacao.DESPESAS -> item.transaction.type == TransactionType.EXPENSE
                    }

                    matchesDate && matchesQuery && matchesCategory && matchesType
                }

                ExtratoTransacoesUiState(
                    transacoes = transacoesFiltradas,
                    buscaQuery = query,
                    categoriaFiltroIds = filterCatIds,
                    tipoFiltroData = tipoData,
                    tipoFiltroTransacao = tipoTransacao,
                    dataInicioTimestamp = inicio,
                    dataFimTimestamp = fim,
                    loading = false
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
        if (categoryId == null) {
            _selectedCategoryIds.value = emptySet()
        } else {
            val current = _selectedCategoryIds.value.toMutableSet()
            if (current.contains(categoryId)) {
                current.remove(categoryId)
            } else {
                current.add(categoryId)
            }
            _selectedCategoryIds.value = current
        }
    }

    fun onTipoFiltroDataChanged(tipo: TipoFiltroData) {
        _tipoFiltroData.value = tipo
    }

    fun onTipoFiltroTransacaoChanged(tipo: TipoFiltroTransacao) {
        _tipoFiltroTransacao.value = tipo
    }

    fun onPeriodoDataChanged(dataInicio: Long?, dataFim: Long?) {
        _dataInicio.value = dataInicio
        _dataFim.value = dataFim
        _tipoFiltroData.value = TipoFiltroData.PERIODO
    }

    fun addTransaction(description: String, amount: BigDecimal, type: TransactionType, categoryId: Long) {
        viewModelScope.launch {
            val transaction = Transaction(
                description = description,
                amount = amount,
                type = type,
                categoryId = categoryId,
                dateTimestamp = System.currentTimeMillis()
            )
            transacaoRepository.insert(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
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
