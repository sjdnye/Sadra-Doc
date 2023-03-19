package com.example.consignmentProject.presentation.consignments


import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.utils.ConsignmentOrder
import com.example.consignmentProject.domain.utils.OrderType

data class ConsignmentState(
    val searchQuery: String = "",
    val consignments: List<Consignment> = emptyList(),
    val consignmentOrder: ConsignmentOrder = ConsignmentOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)