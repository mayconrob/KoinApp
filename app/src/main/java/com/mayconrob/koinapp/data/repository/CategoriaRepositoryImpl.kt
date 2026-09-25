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

import com.mayconrob.koinapp.data.local.CategoryDao
import com.mayconrob.koinapp.data.local.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : ICategoriaRepository {

    override val all: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    override suspend fun insert(category: CategoryEntity): Long {
        return categoryDao.insert(category)
    }

    override suspend fun update(category: CategoryEntity) {
        categoryDao.update(category)
    }

    override suspend fun delete(category: CategoryEntity) {
        categoryDao.delete(category)
    }
}
