package com.atilsamancioglu.artbookhilttesting.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Art::class], version = 1, exportSchema = false)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}
