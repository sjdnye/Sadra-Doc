package com.example.consignmentProject.domain.use_case

import com.example.consignmentProject.domain.repository.ConsignmentRepository

class DeleteAllConsignmentsUseCase(
    private val repository: ConsignmentRepository
) {
    suspend operator fun invoke(){
        repository.deleteAllConsignments()
    }
}