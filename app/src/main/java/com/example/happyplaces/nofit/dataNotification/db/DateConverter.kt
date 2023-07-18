package com.example.happyplaces.nofit.dataNotification.db
import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
     fun fromTimeStamp(value : Long) : Date = Date(value)
    @TypeConverter
    fun dateToTimeStamp(date : Date) : Long = date.time
}