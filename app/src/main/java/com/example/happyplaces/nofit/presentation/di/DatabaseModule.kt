package com.example.happyplaces.nofit.presentation.di
import android.app.Application
import androidx.room.Room
import com.example.happyplaces.nofit.dataNotification.db.TaskCategoryDao
import com.example.happyplaces.nofit.dataNotification.db.TaskDatabaseNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application) : TaskDatabaseNotification {
        return Room.databaseBuilder(app, TaskDatabaseNotification::class.java, "task_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTaskCategoryDao(taskDatabase: TaskDatabaseNotification): TaskCategoryDao {
        return taskDatabase.getTaskCategoryDao()
    }

}