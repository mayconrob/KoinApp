package com.mayconrob.coinflow.domain.enums

import com.mayconrob.coinflow.R

enum class TransactionType(transactionTypeEntry: Int) {
    ENTRY(R.string.transaction_type_entry),      // Entrada
    EXIT(R.string.transaction_type_exit)        // Saída
}