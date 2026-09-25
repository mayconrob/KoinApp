package com.mayconrob.koinapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mayconrob.koinapp.data.local.CategoryEntity
import com.mayconrob.koinapp.data.local.TransactionWithCategory
import com.mayconrob.koinapp.ui.components.AddCategoryDialog
import com.mayconrob.koinapp.ui.components.AddTransactionDialog
import com.mayconrob.koinapp.ui.screens.CategoriesScreen
import com.mayconrob.koinapp.ui.screens.DashboardScreen
import com.mayconrob.koinapp.ui.screens.TransactionListScreen
import com.mayconrob.koinapp.ui.theme.AccentIndigo
import com.mayconrob.koinapp.ui.theme.DarkSurface
import com.mayconrob.koinapp.ui.theme.FinanceAppTheme
import com.mayconrob.koinapp.ui.viewmodel.ExtratoTransacoesViewModel
import com.mayconrob.koinapp.ui.viewmodel.GerenciamentoCategoriasViewModel
import com.mayconrob.koinapp.ui.viewmodel.PainelFinanceiroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent(
    painelViewModel: PainelFinanceiroViewModel = hiltViewModel(),
    extratoViewModel: ExtratoTransacoesViewModel = hiltViewModel(),
    categoriasViewModel: GerenciamentoCategoriasViewModel = hiltViewModel()
) {
    val painelUiState by painelViewModel.uiState.collectAsState()
    val extratoUiState by extratoViewModel.uiState.collectAsState()
    val categoriasUiState by categoriasViewModel.uiState.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddTransactionDialog by remember { mutableStateOf(false) }
    var editingTransaction by remember { mutableStateOf<TransactionWithCategory?>(null) }

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(imageVector = Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentIndigo,
                        selectedTextColor = AccentIndigo,
                        indicatorColor = AccentIndigo.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(imageVector = Icons.AutoMirrored.Filled.ListAlt, contentDescription = "Extrato") },
                    label = { Text("Extrato") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentIndigo,
                        selectedTextColor = AccentIndigo,
                        indicatorColor = AccentIndigo.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(imageVector = Icons.Default.Category, contentDescription = "Categorias") },
                    label = { Text("Categorias") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentIndigo,
                        selectedTextColor = AccentIndigo,
                        indicatorColor = AccentIndigo.copy(alpha = 0.2f)
                    )
                )
            }
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (selectedTab) {
            0 -> DashboardScreen(
                state = painelUiState,
                onAddTransactionClick = { showAddTransactionDialog = true },
                onDeleteTransactionClick = { painelViewModel.deleteTransaction(it) },
                modifier = modifier
            )
            1 -> TransactionListScreen(
                state = extratoUiState,
                categories = categoriasUiState.categorias,
                onSearchQueryChanged = { extratoViewModel.onSearchQueryChanged(it) },
                onCategoryFilterChanged = { extratoViewModel.onCategoryFilterChanged(it) },
                onTipoFiltroDataChanged = { extratoViewModel.onTipoFiltroDataChanged(it) },
                onPeriodoDataChanged = { start, end -> extratoViewModel.onPeriodoDataChanged(start, end) },
                onEditTransactionClick = { editingTransaction = it },
                onDeleteTransactionClick = { extratoViewModel.deleteTransaction(it) },
                modifier = modifier
            )
            2 -> CategoriesScreen(
                state = categoriasUiState,
                onAddCategoryClick = { showAddCategoryDialog = true },
                onEditCategoryClick = { editingCategory = it },
                onDeleteCategoryClick = { categoriasViewModel.deleteCategory(it) },
                modifier = modifier
            )
        }

        if (showAddTransactionDialog || editingTransaction != null) {
            AddTransactionDialog(
                categories = categoriasUiState.categorias,
                transactionToEdit = editingTransaction,
                onDismiss = {
                    showAddTransactionDialog = false
                    editingTransaction = null
                },
                onConfirm = { desc, amount, type, categoryId ->
                    val targetTrans = editingTransaction
                    if (targetTrans != null) {
                        extratoViewModel.updateTransaction(
                            targetTrans.transaction.copy(
                                description = desc,
                                amount = amount,
                                type = type,
                                categoryId = categoryId
                            )
                        )
                    } else {
                        extratoViewModel.addTransaction(desc, amount, type, categoryId)
                    }
                    showAddTransactionDialog = false
                    editingTransaction = null
                }
            )
        }

        if (showAddCategoryDialog || editingCategory != null) {
            AddCategoryDialog(
                categoryToEdit = editingCategory,
                onDismiss = {
                    showAddCategoryDialog = false
                    editingCategory = null
                },
                onConfirm = { name, type, limit, colorHex ->
                    val targetCat = editingCategory
                    if (targetCat != null) {
                        categoriasViewModel.updateCategory(
                            targetCat.copy(
                                name = name,
                                type = type,
                                budgetLimit = limit,
                                colorHex = colorHex
                            )
                        )
                    } else {
                        categoriasViewModel.addCategory(name, type, limit, colorHex)
                    }
                    showAddCategoryDialog = false
                    editingCategory = null
                }
            )
        }
    }
}
