package com.mayconrob.coinflow.domain.repository

import com.mayconrob.coinflow.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategoriaRepository {
    val all: Flow<List<Category>>
    suspend fun insert(category: Category): Long
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
}
