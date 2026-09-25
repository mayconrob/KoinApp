package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.data.local.TransactionWithCategory

enum class TipoFiltroData {
    TODOS,          // Sem filtro de data (Todas as datas)
    ULTIMOS_7_DIAS, // Filtra os últimos 7 dias (sem considerar horário)
    PERIODO         // Filtra um período personalizado (Data Início a Data Fim, sem considerar horário)
}

data class ExtratoTransacoesUiState(
    val transacoes: List<TransactionWithCategory> = emptyList(),
    val buscaQuery: String = "",
    val categoriaFiltroId: Long? = null,
    val tipoFiltroData: TipoFiltroData = TipoFiltroData.TODOS,
    val dataInicioTimestamp: Long? = null,
    val dataFimTimestamp: Long? = null,
    val estaCarregando: Boolean = false
)
