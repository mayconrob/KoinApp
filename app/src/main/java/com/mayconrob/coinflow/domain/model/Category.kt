package com.mayconrob.coinflow.domain.model

import com.mayconrob.coinflow.domain.enums.TransactionType
import java.math.BigDecimal

data class Category(
    val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val budgetLimit: BigDecimal = BigDecimal.ZERO,
    val colorHex: String = "#3F51B5",
    val iconName: String = "Category"
)
