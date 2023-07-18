package com.example.happyplaces.nofit.presentation.di


import com.example.happyplaces.nofit.presentation.adapter.CategoryAdapter
import com.example.happyplaces.nofit.presentation.adapter.TasksAdapterNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonAdapterModule {
    @Provides
    @Singleton
    @Named("base_fragment")
    fun provideTaskAdapterToBaseFragment() = TasksAdapterNotification()

    @Provides
    @Singleton
    @Named("completed_task_fragment")
    fun provideTaskAdapterToCompletedTasksFragment() = TasksAdapterNotification()

    @Provides
    @Singleton
    fun provideCategoryAdapter() = CategoryAdapter()

}