package com.example.consignmentProject.data.repository

import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.data.local.ConsignmentDao
import com.example.consignmentProject.domain.repository.ConsignmentRepository
import kotlinx.coroutines.flow.Flow

class ConsignmentRepositoryImpl(
    private val dao: ConsignmentDao
): ConsignmentRepository {
    override fun getConsignments(query: String): Flow<List<Consignment>> {
        return dao.getConsignments(query = query)
    }
    override suspend fun getConsignmentById(id: Int): Consignment? {
        return dao.getConsignmentById(id = id)
    }
    override suspend fun insertConsignment(consignment: Consignment) {
        dao.insertConsignment(consignment = consignment)
    }
    override suspend fun deleteConsignment(consignment: Consignment) {
        dao.deleteConsignment(consignment = consignment)
    }
    override suspend fun getConsignmentsByYear(year: Int): List<Consignment>? {
        return dao.getConsignmentsByYear(year = year)
    }
    override suspend fun getConsignmentsByMonth(month: Int): List<Consignment>? {
        return dao.getConsignmentsByMonth(month = month)
    }
    override suspend fun getConsignmentsByDay(day: Int): List<Consignment>? {
        return dao.getConsignmentsByDay(day = day)
    }
    override suspend fun getConsignmentsByYearAndMonth(year: Int, month: Int): List<Consignment>? {
        return dao.getConsignmentsByYearAndMonth(year = year, month = month)
    }
    override suspend fun deleteAllConsignments() {
        dao.deleteAllConsignments()
    }
    override suspend fun addAllConsignments(consignments: List<Consignment>) {
        dao.addAllConsignments(consignments = consignments)
    }


}