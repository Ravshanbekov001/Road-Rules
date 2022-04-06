package com.example.yolharakatiqoidalari.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Rules::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun Dao(): MyDAO

    companion object {
        private var instance: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDataBase::class.java, "rules")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}