package com.mayconrob.coinflow.ui.viewmodel

import com.mayconrob.coinflow.domain.model.TransactionWithCategory
import com.mayconrob.coinflow.domain.model.FinancialSummary
import com.mayconrob.coinflow.common.Formatters

data class PainelFinanceiroUiState(
    val resumo: FinancialSummary = FinancialSummary(),
    val consumosOrcamento: List<ConsumoOrcamentoCategoriaUiState> = emptyList(),
    val ultimasTransacoes: List<TransactionWithCategory> = emptyList(),
    val currentMonthLabel: String = Formatters.getCurrentMonthName(),
    val estaCarregando: Boolean = false
)
