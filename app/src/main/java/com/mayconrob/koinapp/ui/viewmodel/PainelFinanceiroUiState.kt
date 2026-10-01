package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.domain.model.TransactionWithCategory
import com.mayconrob.koinapp.domain.model.FinancialSummary

data class PainelFinanceiroUiState(
    val resumo: FinancialSummary = FinancialSummary(),
    val consumosOrcamento: List<ConsumoOrcamentoCategoriaUiState> = emptyList(),
    val ultimasTransacoes: List<TransactionWithCategory> = emptyList(),
    val estaCarregando: Boolean = false
)
