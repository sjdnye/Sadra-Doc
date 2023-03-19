package com.example.consignmentProject.presentation.consignment_add_edit

data class AddEditConsignmentState(
    val title: String = "",
    val weight: String = "",
    val cost:String = "",
    val documentNumber: String = "",
    val year: String = "",
    val month:String = "",
    val day: String = "",
    val fireStoreId: String? = null
)
