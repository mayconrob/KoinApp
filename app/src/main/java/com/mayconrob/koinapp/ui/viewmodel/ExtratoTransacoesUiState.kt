package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.domain.model.TransactionWithCategory

enum class TipoFiltroData {
    ULTIMOS_7_DIAS, // Filtra os últimos 7 dias (sem considerar horário)
    PERIODO         // Filtra um período personalizado (Data Início a Data Fim, sem considerar horário)
}

data class ExtratoTransacoesUiState(
    val transacoes: List<TransactionWithCategory> = emptyList(),
    val buscaQuery: String = "",
    val categoriaFiltroIds: Set<Long> = emptySet(),
    val tipoFiltroData: TipoFiltroData = TipoFiltroData.ULTIMOS_7_DIAS,
    val dataInicioTimestamp: Long? = null,
    val dataFimTimestamp: Long? = null,
    val estaCarregando: Boolean = false
)
