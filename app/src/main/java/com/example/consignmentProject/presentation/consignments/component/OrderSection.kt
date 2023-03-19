package com.example.consignmentProject.presentation.consignments.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.consignmentProject.domain.utils.ConsignmentOrder
import com.example.consignmentProject.domain.utils.OrderType

@Composable
fun OrderSection(
    modifier: Modifier,
    consignmentOrder: ConsignmentOrder = ConsignmentOrder.Date(OrderType.Descending),
    onOrderChange: (ConsignmentOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = consignmentOrder is ConsignmentOrder.Title,
                onSelect = { onOrderChange(ConsignmentOrder.Title(consignmentOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = "Date",
                selected = consignmentOrder is ConsignmentOrder.Date,
                onSelect = { onOrderChange(ConsignmentOrder.Date(consignmentOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = consignmentOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(consignmentOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = "Descending",
                selected = consignmentOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(consignmentOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}