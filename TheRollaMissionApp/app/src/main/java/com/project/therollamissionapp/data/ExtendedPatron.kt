package com.project.therollamissionapp.data

import java.util.*
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

data class ExtendedPatron (
    @SerializedName("_id")
    @Expose
    var id: String = UUID.randomUUID().toString(),
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("birth_date")
    @Expose
    var birth_date: String = "",
    @SerializedName("gender")
    @Expose
    var gender: String = "",
    @SerializedName("phone")
    @Expose
    var phone: String = "",
    @SerializedName("veteran")
    @Expose
    var veteran: Boolean = false,
    @SerializedName("violence")
    @Expose
    var violence: Boolean = false,
    @SerializedName("offender")
    @Expose
    var offender: Boolean = false,
    @SerializedName("date_homeless")
    @Expose
    var date_homeless: String = "",
    @SerializedName("city")
    @Expose
    var city: String = "",
    @SerializedName("reason")
    @Expose
    var reason: String = "",
    // Not exposed to Retrofit/Gson
    var headshotPath: String = "",
    var signaturePath: String = ""
)