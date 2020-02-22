package com.project.therollamissionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.sql.SQLData

@Entity(tableName = "patronCheckIns", primaryKeys = ["patronId", "timestamp"])
data class CheckIn (
    @ColumnInfo(name = "patronId") var patronId: String,
    @ColumnInfo(name = "timestamp") var timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "hasBeenUploaded") var hasBeenUploaded: Boolean = false
)