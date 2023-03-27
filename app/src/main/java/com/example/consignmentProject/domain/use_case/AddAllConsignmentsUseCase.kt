package com.example.consignmentProject.domain.use_case

import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.repository.ConsignmentRepository

class AddAllConsignmentsUseCase(
    private val repository: ConsignmentRepository
) {
    suspend operator fun invoke(consignments: List<Consignment>){
        repository.addAllConsignments(consignments = consignments)
    }
}