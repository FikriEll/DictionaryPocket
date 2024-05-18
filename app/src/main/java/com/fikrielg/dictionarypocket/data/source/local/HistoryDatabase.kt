package com.fikrielg.dictionarypocket.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.util.Converters

@Database(
    version = 1,
    entities = [History::class],
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}