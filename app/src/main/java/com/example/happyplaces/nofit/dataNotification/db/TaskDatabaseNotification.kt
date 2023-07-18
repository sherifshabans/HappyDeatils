package com.example.happyplaces.nofit.dataNotification.db
import androidx.room.*
import com.example.happyplaces.nofit.dataNotification.model.CategoryInfo
import com.example.happyplaces.nofit.dataNotification.model.TaskInfo

@Database(entities = [TaskInfo::class, CategoryInfo::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class TaskDatabaseNotification : RoomDatabase() {
    abstract fun getTaskCategoryDao() : TaskCategoryDao
}