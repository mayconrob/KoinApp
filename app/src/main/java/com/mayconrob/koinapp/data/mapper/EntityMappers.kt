package com.mayconrob.koinapp.data.mapper

import com.mayconrob.koinapp.data.local.CategoryEntity
import com.mayconrob.koinapp.data.local.TransactionEntity
import com.mayconrob.koinapp.data.local.TransactionWithCategoryEntity
import com.mayconrob.koinapp.domain.model.Category
import com.mayconrob.koinapp.domain.model.Transaction
import com.mayconrob.koinapp.domain.model.TransactionWithCategory

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        type = type,
        budgetLimit = budgetLimit,
        colorHex = colorHex,
        iconName = iconName
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        type = type,
        budgetLimit = budgetLimit,
        colorHex = colorHex,
        iconName = iconName
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        description = description,
        amount = amount,
        type = type,
        categoryId = categoryId,
        dateTimestamp = dateTimestamp
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        description = description,
        amount = amount,
        type = type,
        categoryId = categoryId,
        dateTimestamp = dateTimestamp
    )
}

fun TransactionWithCategoryEntity.toDomain(): TransactionWithCategory {
    return TransactionWithCategory(
        transaction = transaction.toDomain(),
        category = category.toDomain()
    )
}

fun TransactionWithCategory.toEntity(): TransactionWithCategoryEntity {
    return TransactionWithCategoryEntity(
        transaction = transaction.toEntity(),
        category = category.toEntity()
    )
}
