package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.data.local.CategoryEntity

data class GerenciamentoCategoriasUiState(
    val categorias: List<CategoryEntity> = emptyList(),
    val estaCarregando: Boolean = false
)
