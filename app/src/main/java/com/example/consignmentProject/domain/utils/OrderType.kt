package  com.example.consignmentProject.domain.utils

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
