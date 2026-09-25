package com.mayconrob.koinapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentIndigo,
    secondary = IncomeGreen,
    tertiary = ExpenseRed,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = TextPrimaryDark,
    onSecondary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

@JvmOverloads
@Composable
fun FinanceAppTheme(
    darkTheme: Boolean = true, // Padrão Dark Mode elegante
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
