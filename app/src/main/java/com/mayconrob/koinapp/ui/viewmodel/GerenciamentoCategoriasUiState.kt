package com.mayconrob.koinapp.ui.viewmodel

import com.mayconrob.koinapp.domain.model.Category

data class GerenciamentoCategoriasUiState(
    val categorias: List<Category> = emptyList(),
    val estaCarregando: Boolean = false
)
