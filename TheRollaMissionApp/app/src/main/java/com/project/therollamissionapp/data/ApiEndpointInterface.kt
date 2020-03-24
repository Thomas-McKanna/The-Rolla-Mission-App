package com.project.therollamissionapp.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpointInterface {
    @POST("patrons/")
    fun createPatron(@Body patron: ExtendedPatron): Call<ResponseBody>

    @Multipart
    @PUT("patrons/headshot/{uuid}")
    fun putHeadshot(@Path("uuid") id: String, @Part image: MultipartBody.Part): Call<ResponseBody>

    @Multipart
    @PUT("patrons/signature/{uuid}")
    fun putSignature(@Path("uuid") id: String, @Part image: MultipartBody.Part): Call<ResponseBody>

    @GET("patrons/headshot/{uuid}")
    fun getHeadshot(@Path("uuid") id: String): Call<Headshot>

    @POST("patrons/checkin/{uuid}")
    fun checkInPatron(@Path("uuid") id: String, @Body checkIn: CheckIn): Call<RequestBody>
}