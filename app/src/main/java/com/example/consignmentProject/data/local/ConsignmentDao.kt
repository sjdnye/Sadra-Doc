package com.example.consignmentProject.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsignmentDao {
    @Query("SELECT * FROM Consignment where LOWER(title) like '%' || LOWER(:query) || '%' ")
    fun getConsignments(query: String): Flow<List<Consignment>>

    @Query("SELECT * FROM consignment where id = :id")
    suspend fun getConsignmentById(id: Int): Consignment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsignment(consignment: Consignment)

    @Delete
    suspend fun deleteConsignment(consignment: Consignment)

    @Query("SELECT * FROM Consignment WHERE year = :year ORDER BY title ASC")
    suspend fun getConsignmentsByYear(year: Int): List<Consignment>?

    @Query("SELECT * FROM Consignment WHERE month = :month ORDER BY title ASC")
    suspend fun getConsignmentsByMonth(month: Int): List<Consignment>?

    @Query("SELECT * FROM Consignment WHERE day = :day ORDER BY title ASC")
    suspend fun getConsignmentsByDay(day: Int): List<Consignment>?
    @Query("SELECT * FROM Consignment WHERE year = :year AND month = :month ORDER BY title ASC")
    suspend fun getConsignmentsByYearAndMonth(year: Int, month: Int): List<Consignment>?

    @Query("Delete FROM Consignment")
    suspend fun deleteAllConsignments()
    @Insert
    suspend fun addAllConsignments(consignments: List<Consignment>)
}