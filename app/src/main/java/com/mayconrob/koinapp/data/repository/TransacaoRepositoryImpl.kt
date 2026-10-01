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

import com.mayconrob.koinapp.data.local.TransactionDao
import com.mayconrob.koinapp.data.mapper.toDomain
import com.mayconrob.koinapp.data.mapper.toEntity
import com.mayconrob.koinapp.domain.model.Transaction
import com.mayconrob.koinapp.domain.enums.TransactionType
import com.mayconrob.koinapp.domain.model.TransactionWithCategory
import com.mayconrob.koinapp.domain.repository.ITransacaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransacaoRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : ITransacaoRepository {

    override val all: Flow<List<TransactionWithCategory>> = transactionDao.getAllWithCategory().map { list ->
        list.map { it.toDomain() }
    }

    override val totalIncome: Flow<BigDecimal?> = all.map { list ->
        list.filter { it.transaction.type == TransactionType.INCOME }
            .fold(BigDecimal.ZERO) { acc, item -> acc.add(item.transaction.amount) }
    }

    override val totalExpenses: Flow<BigDecimal?> = all.map { list ->
        list.filter { it.transaction.type == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, item -> acc.add(item.transaction.amount) }
    }

    override fun getTotalSpentForCategory(categoryId: Long): Flow<BigDecimal?> = all.map { list ->
        list.filter { it.transaction.categoryId == categoryId && it.transaction.type == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, item -> acc.add(item.transaction.amount) }
    }

    override suspend fun insert(transaction: Transaction): Long {
        return transactionDao.insert(transaction.toEntity())
    }

    override suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction.toEntity())
    }

    override suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction.toEntity())
    }
}
