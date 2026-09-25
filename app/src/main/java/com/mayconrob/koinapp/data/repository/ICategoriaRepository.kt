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

import com.mayconrob.koinapp.data.local.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface ICategoriaRepository {
    val all: Flow<List<CategoryEntity>>
    suspend fun insert(category: CategoryEntity): Long
    suspend fun update(category: CategoryEntity)
    suspend fun delete(category: CategoryEntity)
}
