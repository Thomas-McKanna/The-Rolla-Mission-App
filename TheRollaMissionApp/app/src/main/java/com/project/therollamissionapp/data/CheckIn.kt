package com.project.therollamissionapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckIn (
    @SerializedName("patron_id")
    @Expose
    var id: String = "",
    @SerializedName("date")
    @Expose
    var date: String = ""
)
