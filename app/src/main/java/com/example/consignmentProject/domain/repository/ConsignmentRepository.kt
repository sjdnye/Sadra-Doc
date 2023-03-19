package com.example.consignmentProject.domain.repository

import com.example.consignmentProject.data.local.Consignment
import kotlinx.coroutines.flow.Flow

interface ConsignmentRepository {

    fun getConsignments(query: String): Flow<List<Consignment>>

    suspend fun getConsignmentById(id: Int): Consignment?

    suspend fun insertConsignment(consignment: Consignment)

    suspend fun deleteConsignment(consignment: Consignment)

    suspend fun getConsignmentsByYear(year: Int): List<Consignment>?

    suspend fun getConsignmentsByMonth(month: Int): List<Consignment>?

    suspend fun getConsignmentsByDay(day: Int): List<Consignment>?
}
