package com.example.happyplaces.nofit.presentation.di

import com.example.happyplaces.nofit.dataNotification.db.TaskCategoryDao
import com.example.happyplaces.nofit.dataNotification.repository.TaskCategoryRepositoryImpl
import com.example.happyplaces.nofit.domain.TaskCategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskCategoryRepository(taskCategoryDao: TaskCategoryDao) : TaskCategoryRepository {
        return TaskCategoryRepositoryImpl(taskCategoryDao)
    }
}