package com.example.consignmentProject.domain.use_case

import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.data.local.InvalidInputException
import com.example.consignmentProject.domain.repository.ConsignmentRepository


class InsertConsignmentUseCase(
    private val repository: ConsignmentRepository
) {

    @Throws(InvalidInputException::class)
    suspend operator fun invoke(consignment: Consignment) {
        repository.insertConsignment(consignment = consignment)
    }
}