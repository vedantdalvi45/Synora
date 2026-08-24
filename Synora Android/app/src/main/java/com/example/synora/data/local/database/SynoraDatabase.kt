package com.example.synora.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.synora.data.local.dao.UserDao
import com.example.synora.data.local.entity.UserEntity

// Entities will be added per feature in later phases.
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class SynoraDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        fun create(context: Context): SynoraDatabase =
            Room.databaseBuilder(context, SynoraDatabase::class.java, "synora.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
