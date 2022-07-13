package com.example.summerproject.domain.utils

sealed class ArticleOrder(val orderType: OrderType){
    class Title(orderType: OrderType): ArticleOrder(orderType)
    class Date(orderType: OrderType): ArticleOrder(orderType)

    fun copy(orderType: OrderType): ArticleOrder{
        return when(this){
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}