package com.project.therollamissionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "patrons")
data class Patron (
    @SerializedName("_id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "patronId")
    var id: String = UUID.randomUUID().toString(),
    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    var name: String = ""
)
