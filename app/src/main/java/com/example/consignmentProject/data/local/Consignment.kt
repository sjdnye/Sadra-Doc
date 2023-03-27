package com.example.consignmentProject.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Consignment(
    @PrimaryKey
    val id: Int? = null,
    var firestoreId: String? = null,
    val publisherId: String = "",
    val documentNumber: Long? = null,
    val day: Int? = null,
    val month: Int? = null,
    val year: Int? = null,
    val title: String = "",
    val weight: Double? = null,
    val cost: Long? = null,
    val consignmentAddTimeToDatabase: Long = 0,
): Parcelable


class InvalidInputException(error: String): Exception(error)