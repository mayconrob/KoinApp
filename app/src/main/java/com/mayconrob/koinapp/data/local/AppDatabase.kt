/*
 * KoinApp - Gestão Financeira Pessoal
 * Copyright (C) 2026 Maycon Roberto - GitHub: @mayconrob
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package com.mayconrob.koinapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mayconrob.koinapp.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fincontrol_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialCategories(database.categoryDao())
                    }
                }
            }

            suspend fun populateInitialCategories(categoryDao: CategoryDao) {
                val defaultCategories = listOf(
                    CategoryEntity(name = "Salário", type = TransactionType.INCOME, colorHex = "#4CAF50"),
                    CategoryEntity(name = "Freelance", type = TransactionType.INCOME, colorHex = "#00BCD4"),
                    CategoryEntity(name = "Alimentação", type = TransactionType.EXPENSE, budgetLimit = BigDecimal("800.00"), colorHex = "#FF9800"),
                    CategoryEntity(name = "Transporte", type = TransactionType.EXPENSE, budgetLimit = BigDecimal("350.00"), colorHex = "#3F51B5"),
                    CategoryEntity(name = "Moradia", type = TransactionType.EXPENSE, budgetLimit = BigDecimal("1500.00"), colorHex = "#9C27B0"),
                    CategoryEntity(name = "Lazer", type = TransactionType.EXPENSE, budgetLimit = BigDecimal("400.00"), colorHex = "#E91E63"),
                    CategoryEntity(name = "Saúde", type = TransactionType.EXPENSE, budgetLimit = BigDecimal("300.00"), colorHex = "#F44336")
                )
                categoryDao.insert(defaultCategories)
            }
        }
    }
}
