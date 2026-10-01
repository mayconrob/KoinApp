package com.mayconrob.coinflow.domain.model

import java.math.BigDecimal

data class FinancialSummary(
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val currentBalance: BigDecimal = BigDecimal.ZERO,
    val savingsPercentage: BigDecimal = BigDecimal.ZERO
)