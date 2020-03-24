package com.project.therollamissionapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Headshot (
    @SerializedName("headshot")
    @Expose
    var url: String = ""
)