package com.example.consignmentProject.presentation.consignments

import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.utils.ConsignmentOrder

sealed class ConsignmentEvent {
    data class Order(val consignmentOrder: ConsignmentOrder) : ConsignmentEvent()
    data class DeleteConsignment(val consignment: Consignment) : ConsignmentEvent()
    data class SearchConsignment(val query: String) : ConsignmentEvent()
    object RestoreConsignment : ConsignmentEvent()
    object ToggleOrderSection : ConsignmentEvent()
    object LogOut : ConsignmentEvent()
    object Refresh : ConsignmentEvent()
}