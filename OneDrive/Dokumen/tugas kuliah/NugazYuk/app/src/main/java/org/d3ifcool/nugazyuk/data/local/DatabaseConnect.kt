package org.d3ifcool.nugazyuk.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Token::class, Task::class], version = 2, exportSchema = false)
abstract class NugazlahDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: NugazlahDatabase? = null
        fun getInstance(context: Context): NugazlahDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NugazlahDatabase::class.java, "Nugazlah.db"
                ).build()
            }
    }
}