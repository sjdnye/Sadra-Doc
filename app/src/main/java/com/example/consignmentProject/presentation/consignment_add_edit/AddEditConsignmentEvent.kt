package com.example.consignmentProject.presentation.consignment_add_edit

sealed class AddEditConsignmentEvent{
    data class ChangeTitle(val title:String): AddEditConsignmentEvent()
    data class ChangeWeight(val weight:String): AddEditConsignmentEvent()
    data class ChangeCost(val cost:String): AddEditConsignmentEvent()
    data class ChangeDocumentNumber(val documentNumber:String): AddEditConsignmentEvent()
    data class ChangeYear(val year:String): AddEditConsignmentEvent()
    data class ChangeMonth(val month:String): AddEditConsignmentEvent()
    data class ChangeDay(val day: String): AddEditConsignmentEvent()
    object SaveConsignment : AddEditConsignmentEvent()

}