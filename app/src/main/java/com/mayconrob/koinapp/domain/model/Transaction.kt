package com.mayconrob.koinapp.domain.model

import com.mayconrob.koinapp.domain.enums.TransactionType
import java.math.BigDecimal

data class Transaction(
    val id: Long = 0,
    val description: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val categoryId: Long,
    val dateTimestamp: Long
)
