package com.mayconrob.koinapp.domain.repository

import com.mayconrob.koinapp.domain.model.Transaction
import com.mayconrob.koinapp.domain.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface ITransacaoRepository {
    val all: Flow<List<TransactionWithCategory>>
    val totalIncome: Flow<BigDecimal?>
    val totalExpenses: Flow<BigDecimal?>
    fun getTotalSpentForCategory(categoryId: Long): Flow<BigDecimal?>
    suspend fun insert(transaction: Transaction): Long
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
    suspend fun hasTransactionsForCategory(categoryId: Long): Boolean
}
