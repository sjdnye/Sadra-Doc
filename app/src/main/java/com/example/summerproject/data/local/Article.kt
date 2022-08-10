package com.example.summerproject.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Article(
    @PrimaryKey
    val id: Int? = null,
    val authorName_1: String = "",
    val authorFamily_1: String = "",
    val authorAffiliation_1: String = "",
    val authorName_2: String = "",
    val authorFamily_2: String = "",
    val authorAffiliation_2: String = "",
    val authorName_3: String = "",
    val authorFamily_3: String = "",
    val authorAffiliation_3: String = "",
    val articleType: String = "",
    val articleTitle: String = "",
    val persianTitle: String = "",
    val englishTitle: String = "",
    val placeOfPrinting: String = "",
    val year: String = "",
    val articleAddTimeToDatabase: Long = 0,
    val vol: String = "",
    val No: String = "",
    val institute: String = "",
    val content: String = "",
    @get:Exclude
    var fireStoreId:String? = null
): Parcelable{

}


class InvalidInputException(error: String): Exception(error)