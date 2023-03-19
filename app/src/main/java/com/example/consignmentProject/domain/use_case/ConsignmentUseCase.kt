package com.example.consignmentProject.domain.use_case

data class ConsignmentUseCase(
    val deleteConsignmentUseCase: DeleteConsignmentUseCase,
    val insertConsignmentUseCase: InsertConsignmentUseCase,
    val getConsignmentUseCase: GetConsignmentUseCase,
    val getConsignmentByIdUseCase: GetConsignmentByIdUseCase
)