package com.example.consignmentProject.domain.use_case


import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.repository.ConsignmentRepository

class DeleteConsignmentUseCase(
    private val repository: ConsignmentRepository
) {
    suspend operator fun invoke(consignment: Consignment){
        repository.deleteConsignment(consignment = consignment)
    }
}