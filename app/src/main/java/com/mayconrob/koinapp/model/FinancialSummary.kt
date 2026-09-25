package com.mayconrob.koinapp.model

import java.math.BigDecimal

enum class TransactionType {
    INCOME,  // Receita / Entrada
    EXPENSE  // Despesa / Saída
}

data class FinancialSummary(
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val currentBalance: BigDecimal = BigDecimal.ZERO,
    val savingsPercentage: BigDecimal = BigDecimal.ZERO
)
