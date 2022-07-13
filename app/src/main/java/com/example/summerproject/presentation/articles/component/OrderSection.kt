package com.example.summerproject.presentation.articles.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.summerproject.presentation.articles.component.DefaultRadioButton
import com.example.summerproject.domain.utils.ArticleOrder
import com.example.summerproject.domain.utils.OrderType

@Composable
fun OrderSection(
    modifier: Modifier,
    articleOrder: ArticleOrder = ArticleOrder.Date(OrderType.Descending),
    onOrderChange: (ArticleOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = articleOrder is ArticleOrder.Title,
                onSelect = { onOrderChange(ArticleOrder.Title(articleOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = "Date",
                selected = articleOrder is ArticleOrder.Date,
                onSelect = { onOrderChange(ArticleOrder.Date(articleOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = articleOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(articleOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = "Descending",
                selected = articleOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(articleOrder.copy(OrderType.Descending))
                }
            )
        }

    }

}