/*
 * KoinApp - Gestão Financeira Pessoal
 * Copyright (C) 2026 Maycon Roberto GitHub: @mayconrob
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mayconrob.koinapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayconrob.koinapp.data.local.CategoryEntity
import com.mayconrob.koinapp.data.repository.ICategoriaRepository
import com.mayconrob.koinapp.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class GerenciamentoCategoriasViewModel @Inject constructor(
    private val categoriaRepository: ICategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GerenciamentoCategoriasUiState())
    val uiState: StateFlow<GerenciamentoCategoriasUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoriaRepository.all.collect { categories ->
                _uiState.value = GerenciamentoCategoriasUiState(
                    categorias = categories,
                    estaCarregando = false
                )
            }
        }
    }

    fun addCategory(name: String, type: TransactionType, budgetLimit: BigDecimal, colorHex: String) {
        viewModelScope.launch {
            val category = CategoryEntity(
                name = name,
                type = type,
                budgetLimit = budgetLimit,
                colorHex = colorHex
            )
            categoriaRepository.insert(category)
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoriaRepository.update(category)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoriaRepository.delete(category)
        }
    }
}
