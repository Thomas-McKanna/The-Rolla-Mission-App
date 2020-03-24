package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.*
import com.project.therollamissionapp.data.source.local.PatronDao
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import java.lang.Exception
import java.net.ConnectException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DefaultPatronRepository @Inject constructor (
    private val patronDao: PatronDao,
    private val retrofitService: ApiEndpointInterface
) : PatronRepository {
    override suspend fun createPatron(extendedPatron: ExtendedPatron): Result<Unit> {
        val headshotPart = getMultipartFileData("headshot", extendedPatron.headshotPath)
        val signaturePart = getMultipartFileData("signature", extendedPatron.signaturePath)
        val calls = listOf(
            retrofitService.createPatron(extendedPatron),
            retrofitService.putHeadshot(extendedPatron.id, headshotPart),
            retrofitService.putSignature(extendedPatron.id, signaturePart)
        )
        for (call in calls) {
            val result = sendCall(call)
            if (result is Result.Error) {
                return result
            }
        }
        val patron = Patron(
            extendedPatron.name,
            extendedPatron.id
        )
        patronDao.insertPatron(patron)
        return Result.Success(Unit)
    }

    override suspend fun getPatronsWithName(name: String): Result<List<Patron>> {
        if (name.isEmpty()) {
            return Result.Success(emptyList())
        } else {
            val patrons = patronDao.getPatronsByName("%${name}%")
            return Result.Success(patrons)
        }
    }

    override suspend fun insertPatron(patron: Patron) {
        patronDao.insertPatron(patron)
    }

    override suspend fun checkIn(patron: Patron): Result<Unit> {
        val checkIn = CheckIn(patron.id, ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT))
        val call = retrofitService.createCheckIn(checkIn)
        return sendCall(call)
    }

    override suspend fun deletePatron(patron: Patron) {
        withContext(Dispatchers.IO) {
            launch { patronDao.deletePatron(patron) }
        }
    }

    private suspend fun sendCall(call: Call<ResponseBody>): Result<Unit> {
        try {
            val response = withContext(Dispatchers.IO) {
                call.execute()
            }
            if (response.isSuccessful) {
                return Result.Success(Unit)
            } else {
                return Result.Error(Exception(response.errorBody().toString()))
            }
        } catch (e: ConnectException) {
            return Result.Error(Exception(e.toString()))
        }
    }

    private fun getMultipartFileData(field: String, absPath: String): MultipartBody.Part {
        val MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg")
        val file = File(absPath)
        val requestBody = RequestBody.create(MEDIA_TYPE_JPEG, file)
        return MultipartBody.Part.createFormData(field, file.name, requestBody)
    }
}