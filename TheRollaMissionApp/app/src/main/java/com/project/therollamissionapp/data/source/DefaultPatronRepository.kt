package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.ApiEndpointInterface
import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.source.local.PatronDao
import kotlinx.coroutines.*
import okhttp3.*
import java.io.File
import java.lang.Exception
import java.net.ConnectException
import javax.inject.Inject

class DefaultPatronRepository @Inject constructor (
    private val patronDao: PatronDao,
    private val retrofitService: ApiEndpointInterface
) : PatronRepository {
    override suspend fun createPatron(extendedPatron: ExtendedPatron): Result<Unit> {
        val headshotPart = getMultipartFileData("headshot", extendedPatron.headshotPath)
        val signaturePart = getMultipartFileData("signature", extendedPatron.signaturePath)
        val requests = listOf(
            retrofitService.createPatron(extendedPatron),
            retrofitService.putHeadshot(extendedPatron.id, headshotPart),
            retrofitService.putSignature(extendedPatron.id, signaturePart)
        )
        for (request in requests) {
            try {
                val response = withContext(Dispatchers.IO) {
                    request.execute()
                }
                if (!response.isSuccessful) {
                    return Result.Error(Exception(response.errorBody().toString()))
                }
            } catch (e: ConnectException) {
                return Result.Error(Exception(e.toString()))
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
        // TODO: Attempt to upload checkin instance to external web service.
        delay(2000) // simulate upload time
        return Result.Success(Unit)
    }

    override suspend fun deletePatron(patron: Patron) {
        withContext(Dispatchers.IO) {
            launch { patronDao.deletePatron(patron) }
        }
    }

    private fun getMultipartFileData(field: String, absPath: String): MultipartBody.Part {
        val MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg")
        val file = File(absPath)
        val requestBody = RequestBody.create(MEDIA_TYPE_JPEG, file)
        return MultipartBody.Part.createFormData(field, file.name, requestBody)
    }
}