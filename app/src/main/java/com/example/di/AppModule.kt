package com.example.di

import android.app.Application
import androidx.room.Room
import com.example.summerproject.data.local.ArticleDao
import com.example.summerproject.data.local.ArticleDatabase
import com.example.summerproject.data.repository.ArticleRepositoryImpl
import com.example.summerproject.domain.repository.ArticleRepository
import com.example.summerproject.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(app:Application) : ArticleDatabase{
        return Room.databaseBuilder(
            app,
            ArticleDatabase::class.java,
            ArticleDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(articleDatabase: ArticleDatabase): ArticleDao{
        return articleDatabase.articleDao
    }

    @Provides
    @Singleton
    fun provideArticleRepository(dao:ArticleDao): ArticleRepository{
        return ArticleRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideArticleUseCase(articleRepository:ArticleRepository) : ArticleUseCase{
        return ArticleUseCase(
            getArticlesUseCase = GetArticlesUseCase(articleRepository),
            getArticleByIdUseCase = GetArticleByIdUseCase(articleRepository),
            insertArticleUseCase = InsertArticleUseCase(articleRepository),
            deleteArticleUseCase = DeleteArticleUseCase(articleRepository)
        )
    }

}