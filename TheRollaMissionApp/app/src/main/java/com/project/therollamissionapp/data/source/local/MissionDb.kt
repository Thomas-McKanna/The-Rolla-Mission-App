package com.project.therollamissionapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.therollamissionapp.data.CheckIn
import com.project.therollamissionapp.data.Patron

@Database(entities = arrayOf(Patron::class, CheckIn::class), version = 1)
abstract class MissionDb : RoomDatabase() {
    abstract fun patronDao(): PatronDao
}