package  com.example.summerproject.domain.utils

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
