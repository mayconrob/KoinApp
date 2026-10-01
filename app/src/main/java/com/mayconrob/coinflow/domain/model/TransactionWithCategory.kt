package com.mayconrob.coinflow.domain.model

data class TransactionWithCategory(
    val transaction: Transaction,
    val category: Category
)
