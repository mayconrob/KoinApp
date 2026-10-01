package com.mayconrob.koinapp.data.repository.di

import com.mayconrob.koinapp.data.repository.CategoriaRepositoryImpl
import com.mayconrob.koinapp.data.repository.TransacaoRepositoryImpl
import com.mayconrob.koinapp.domain.repository.ICategoriaRepository
import com.mayconrob.koinapp.domain.repository.ITransacaoRepository
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