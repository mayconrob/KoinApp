/*
 * KoinApp - Gestão Financeira Pessoal
 * Copyright (C) 2026 Maycon Roberto @mayconrob
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mayconrob.koinapp.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Formatters {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
    private val shortDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    private val monthYearFormat = SimpleDateFormat("MMMM 'de' yyyy", Locale("pt", "BR"))

    fun formatCurrency(amount: BigDecimal?): String {
        val safeAmount = amount ?: BigDecimal.ZERO
        return currencyFormat.format(safeAmount)
    }

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatShortDate(timestamp: Long): String {
        return shortDateFormat.format(Date(timestamp))
    }

    fun getCurrentMonthName(): String {
        val name = monthYearFormat.format(Date())
        return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("pt", "BR")) else it.toString() }
    }

    fun getCurrentMonthRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        // Início do Mês: Dia 1 às 00:00:00.000
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis

        // Fim do Mês: Último dia às 23:59:59.999
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfMonth = calendar.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }

    fun getLast7DaysRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        // Fim de Hoje: 23:59:59.999
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfToday = calendar.timeInMillis

        // Início de 7 dias atrás (incluindo hoje): 00:00:00.000
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOf7DaysAgo = calendar.timeInMillis

        return Pair(startOf7DaysAgo, endOfToday)
    }

    fun getStartOfDay(timestamp: Long): Long {
        val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCal.timeInMillis = timestamp
        val year = utcCal.get(Calendar.YEAR)
        val month = utcCal.get(Calendar.MONTH)
        val day = utcCal.get(Calendar.DAY_OF_MONTH)

        val localCal = Calendar.getInstance()
        localCal.set(year, month, day, 0, 0, 0)
        localCal.set(Calendar.MILLISECOND, 0)
        return localCal.timeInMillis
    }

    fun getEndOfDay(timestamp: Long): Long {
        val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCal.timeInMillis = timestamp
        val year = utcCal.get(Calendar.YEAR)
        val month = utcCal.get(Calendar.MONTH)
        val day = utcCal.get(Calendar.DAY_OF_MONTH)

        val localCal = Calendar.getInstance()
        localCal.set(year, month, day, 23, 59, 59)
        localCal.set(Calendar.MILLISECOND, 999)
        return localCal.timeInMillis
    }
}
