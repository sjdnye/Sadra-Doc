package com.example.summerproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Article::class],
    version = 4
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract val articleDao : ArticleDao

    companion object {
        const val DATABASE_NAME = "articles_db"
    }
}