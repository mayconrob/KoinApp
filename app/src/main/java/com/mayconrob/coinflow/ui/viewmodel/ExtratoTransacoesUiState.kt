package com.mayconrob.coinflow.ui.viewmodel

import com.mayconrob.coinflow.domain.model.TransactionWithCategory

enum class TipoFiltroData {
    HOJE,           // Filtra o dia de hoje (sem considerar horário)
    ULTIMOS_7_DIAS, // Filtra os últimos 7 dias (sem considerar horário)
    PERIODO         // Filtra um período personalizado (Data Início a Data Fim, sem considerar horário)
}

data class ExtratoTransacoesUiState(
    val transacoes: List<TransactionWithCategory> = emptyList(),
    val buscaQuery: String = "",
    val categoriaFiltroIds: Set<Long> = emptySet(),
    val tipoFiltroData: TipoFiltroData = TipoFiltroData.HOJE,
    val dataInicioTimestamp: Long? = null,
    val dataFimTimestamp: Long? = null,
    val loading: Boolean = false
)
