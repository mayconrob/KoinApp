package com.mayconrob.coinflow.data.repository.di

import com.mayconrob.coinflow.data.repository.CategoriaRepositoryImpl
import com.mayconrob.coinflow.data.repository.TransacaoRepositoryImpl
import com.mayconrob.coinflow.domain.repository.ICategoriaRepository
import com.mayconrob.coinflow.domain.repository.ITransacaoRepository
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