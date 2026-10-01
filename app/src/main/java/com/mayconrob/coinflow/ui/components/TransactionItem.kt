package com.mayconrob.coinflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mayconrob.coinflow.domain.model.TransactionWithCategory
import com.mayconrob.coinflow.domain.enums.TransactionType
import com.mayconrob.coinflow.ui.theme.DarkCardBorder
import com.mayconrob.coinflow.ui.theme.DarkSurface
import com.mayconrob.coinflow.ui.theme.ExpenseRed
import com.mayconrob.coinflow.ui.theme.IncomeGreen
import com.mayconrob.coinflow.common.Formatters

@Composable
fun TransactionItem(
    item: TransactionWithCategory,
    modifier: Modifier = Modifier,
    onEditClick: ((TransactionWithCategory) -> Unit)? = null,
    onDeleteClick: ((TransactionWithCategory) -> Unit)? = null
) {
    val isIncome = item.transaction.type == TransactionType.INCOME
    val amountPrefix = if (isIncome) "+ " else "- "
    val amountColor = if (isIncome) IncomeGreen else ExpenseRed

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Column {
                if (item.transaction.description.isNotBlank()) {
                    Text(
                        text = item.transaction.description,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.category.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = amountColor
                    )
                    Text(
                        text = " • " + Formatters.formatShortDate(item.transaction.dateTimestamp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = amountPrefix + Formatters.formatCurrency(item.transaction.amount),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = amountColor
            )

            if (onEditClick != null || onDeleteClick != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-4).dp)
                ) {
                    if (onEditClick != null) {
                        IconButton(
                            onClick = { onEditClick(item) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar transação ${item.transaction.description.ifBlank { item.category.name }}",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    if (onDeleteClick != null) {
                        IconButton(
                            onClick = { onDeleteClick(item) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir transação ${item.transaction.description.ifBlank { item.category.name }}",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
