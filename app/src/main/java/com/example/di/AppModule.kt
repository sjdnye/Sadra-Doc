package com.example.di

import android.app.Application
import androidx.room.Room
import com.example.consignmentProject.data.local.*
import com.example.consignmentProject.data.repository.ConsignmentRepositoryImpl
import com.example.consignmentProject.domain.repository.ConsignmentRepository
import com.example.consignmentProject.domain.use_case.*
import com.example.consignmentProject.utils.ConnectivityObserver
import com.example.consignmentProject.utils.NetworkConnectivityObserver
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
    fun provideConsignmentDatabase(app:Application) : ConsignmentDatabase{
        return Room.databaseBuilder(
            app,
            ConsignmentDatabase::class.java,
            ConsignmentDatabase.DATABASE_NAME
        ).build()
    }
    @Provides
    @Singleton
    fun provideConsignmentDao(consignmentDatabase: ConsignmentDatabase): ConsignmentDao{
        return consignmentDatabase.consignmentDao
    }
    @Provides
    @Singleton
    fun provideConsignmentRepository(dao:ConsignmentDao): ConsignmentRepository{
        return ConsignmentRepositoryImpl(dao = dao)
    }
    @Provides
    @Singleton
    fun provideArticleUseCase(consignmentRepository: ConsignmentRepository) : ConsignmentUseCase{
        return ConsignmentUseCase(
            getConsignmentUseCase = GetConsignmentUseCase(consignmentRepository),
            getConsignmentByIdUseCase = GetConsignmentByIdUseCase(consignmentRepository),
            insertConsignmentUseCase = InsertConsignmentUseCase(consignmentRepository),
            deleteConsignmentUseCase = DeleteConsignmentUseCase(consignmentRepository)
        )
    }
    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(app: Application) : ConnectivityObserver{
        return NetworkConnectivityObserver(app)
    }
}