package com.example.smb1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smb1.Models.ModelDao
import com.example.smb1.Models.ModelDto

@Database(
    entities = [
        ModelDto::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val modelBase: ModelDao
}