package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.data.local.CategoryEntity
import java.math.BigDecimal

data class ConsumoOrcamentoCategoriaUiState(
    val categoria: CategoryEntity,
    val totalGasto: BigDecimal = BigDecimal.ZERO,
    val porcentagemConsumida: BigDecimal = BigDecimal.ZERO
)
