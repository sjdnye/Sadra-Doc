package com.example.consignmentProject.domain.use_case

import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.repository.ConsignmentRepository


class GetConsignmentByIdUseCase(
    private val repository: ConsignmentRepository
) {
    suspend operator fun invoke(id: Int): Consignment? {
        return repository.getConsignmentById(id = id)
    }
}