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
import com.mayconrob.koinapp.data.local.TransactionWithCategory
import com.mayconrob.koinapp.data.repository.ICategoriaRepository
import com.mayconrob.koinapp.data.repository.ITransacaoRepository
import com.mayconrob.koinapp.model.FinancialSummary
import com.mayconrob.koinapp.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class PainelFinanceiroViewModel @Inject constructor(
    private val categoriaRepository: ICategoriaRepository,
    private val transacaoRepository: ITransacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PainelFinanceiroUiState())
    val uiState: StateFlow<PainelFinanceiroUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                transacaoRepository.all,
                categoriaRepository.all
            ) { transactions, categories ->

                val income = transactions
                    .filter { it.transaction.type == TransactionType.INCOME }
                    .fold(BigDecimal.ZERO) { acc, item -> acc.add(item.transaction.amount) }

                val expenses = transactions
                    .filter { it.transaction.type == TransactionType.EXPENSE }
                    .fold(BigDecimal.ZERO) { acc, item -> acc.add(item.transaction.amount) }

                val balance = income.subtract(expenses)

                val savingsPct = if (income > BigDecimal.ZERO) {
                    income.subtract(expenses)
                        .multiply(BigDecimal(100))
                        .divide(income, 2, RoundingMode.HALF_UP)
                } else {
                    BigDecimal.ZERO
                }

                val summary = FinancialSummary(
                    totalIncome = income,
                    totalExpenses = expenses,
                    currentBalance = balance,
                    savingsPercentage = savingsPct
                )

                val consumosOrcamento = categories
                    .filter { it.type == TransactionType.EXPENSE && it.budgetLimit > BigDecimal.ZERO }
                    .map { category ->
                        val spent = transactions
                            .filter { it.transaction.categoryId == category.id && it.transaction.type == TransactionType.EXPENSE }
                            .fold(BigDecimal.ZERO) { acc, item -> acc.add(item.transaction.amount) }

                        val pct = if (category.budgetLimit > BigDecimal.ZERO) {
                            spent.multiply(BigDecimal(100))
                                .divide(category.budgetLimit, 2, RoundingMode.HALF_UP)
                        } else {
                            BigDecimal.ZERO
                        }

                        ConsumoOrcamentoCategoriaUiState(
                            categoria = category,
                            totalGasto = spent,
                            porcentagemConsumida = pct
                        )
                    }

                PainelFinanceiroUiState(
                    resumo = summary,
                    consumosOrcamento = consumosOrcamento,
                    ultimasTransacoes = transactions.take(5),
                    estaCarregando = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun deleteTransaction(item: TransactionWithCategory) {
        viewModelScope.launch {
            transacaoRepository.delete(item.transaction)
        }
    }
}
