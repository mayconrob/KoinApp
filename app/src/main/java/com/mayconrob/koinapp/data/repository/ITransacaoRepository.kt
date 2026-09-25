/*
 * KoinApp - Gestão Financeira Pessoal
 * Copyright (C) 2026 Maycon Roberto - GitHub: @mayconrob
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mayconrob.koinapp.data.repository

import com.mayconrob.koinapp.data.local.TransactionEntity
import com.mayconrob.koinapp.data.local.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface ITransacaoRepository {
    val all: Flow<List<TransactionWithCategory>>
    val totalIncome: Flow<BigDecimal?>
    val totalExpenses: Flow<BigDecimal?>
    fun getTotalSpentForCategory(categoryId: Long): Flow<BigDecimal?>
    suspend fun insert(transaction: TransactionEntity): Long
    suspend fun update(transaction: TransactionEntity)
    suspend fun delete(transaction: TransactionEntity)
}
