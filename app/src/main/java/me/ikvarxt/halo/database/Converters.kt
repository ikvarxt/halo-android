package me.ikvarxt.halo.database

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(millis: Long): Calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
}