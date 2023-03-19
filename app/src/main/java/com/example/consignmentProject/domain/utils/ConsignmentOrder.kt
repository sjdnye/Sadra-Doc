package com.example.consignmentProject.domain.utils

sealed class ConsignmentOrder(val orderType: OrderType){
    class Title(orderType: OrderType): ConsignmentOrder(orderType)
    class Date(orderType: OrderType): ConsignmentOrder(orderType)

    fun copy(orderType: OrderType): ConsignmentOrder{
        return when(this){
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}