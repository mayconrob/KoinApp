package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.data.local.TransactionWithCategory
import com.mayconrob.koinapp.model.FinancialSummary

data class PainelFinanceiroUiState(
    val resumo: FinancialSummary = FinancialSummary(),
    val consumosOrcamento: List<ConsumoOrcamentoCategoriaUiState> = emptyList(),
    val ultimasTransacoes: List<TransactionWithCategory> = emptyList(),
    val estaCarregando: Boolean = false
)
