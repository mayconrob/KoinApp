/*
 * KoinApp - Gestão Financeira Pessoal
 * Copyright (C) 2026 Maycon Roberto GitHub: @mayconrob
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mayconrob.koinapp.di

import com.mayconrob.koinapp.data.repository.CategoriaRepositoryImpl
import com.mayconrob.koinapp.data.repository.ICategoriaRepository
import com.mayconrob.koinapp.data.repository.ITransacaoRepository
import com.mayconrob.koinapp.data.repository.TransacaoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCategoriaRepository(
        impl: CategoriaRepositoryImpl
    ): ICategoriaRepository

    @Binds
    @Singleton
    abstract fun bindTransacaoRepository(
        impl: TransacaoRepositoryImpl
    ): ITransacaoRepository
}
