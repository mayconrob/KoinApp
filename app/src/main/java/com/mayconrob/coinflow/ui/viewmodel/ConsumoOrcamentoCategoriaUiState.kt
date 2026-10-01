package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.domain.model.Category
import java.math.BigDecimal

data class ConsumoOrcamentoCategoriaUiState(
    val categoria: Category,
    val totalGasto: BigDecimal = BigDecimal.ZERO,
    val porcentagemConsumida: BigDecimal = BigDecimal.ZERO
)
