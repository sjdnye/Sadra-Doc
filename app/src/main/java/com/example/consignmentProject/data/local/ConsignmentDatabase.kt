package com.example.consignmentProject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Consignment::class],
    version = 1
)
abstract class ConsignmentDatabase: RoomDatabase() {

    abstract val consignmentDao : ConsignmentDao

    companion object {
        const val DATABASE_NAME = "consignments_db"
    }
}