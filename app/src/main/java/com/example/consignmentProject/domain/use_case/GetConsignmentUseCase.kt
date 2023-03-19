package com.example.consignmentProject.domain.use_case


import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.repository.ConsignmentRepository
import com.example.consignmentProject.domain.utils.ConsignmentOrder
import com.example.consignmentProject.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetConsignmentUseCase(
    private val repository: ConsignmentRepository
) {
    operator fun invoke(
        query: String,
        consignmentOrder: ConsignmentOrder = ConsignmentOrder.Date(OrderType.Descending)
    ): Flow<List<Consignment>> {
        return repository.getConsignments(query).map { consignments ->
            when (consignmentOrder.orderType) {
                is OrderType.Ascending -> {
                    when (consignmentOrder) {
                        is ConsignmentOrder.Title -> consignments.sortedBy { it.title.lowercase() }
                        is ConsignmentOrder.Date -> consignments.sortedBy { it.year }
                            .sortedBy { it.month }.sortedBy { it.day }
                    }
                }
                is OrderType.Descending -> {
                    when (consignmentOrder) {
                        is ConsignmentOrder.Title -> consignments.sortedByDescending { it.title.lowercase() }
                        is ConsignmentOrder.Date -> consignments.sortedByDescending { it.year }
                            .sortedByDescending { it.month }.sortedByDescending { it.day }
                    }
                }
            }
        }
    }
}