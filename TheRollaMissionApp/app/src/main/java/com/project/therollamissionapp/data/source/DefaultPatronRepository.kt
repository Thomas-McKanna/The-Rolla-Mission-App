package com.project.therollamissionapp.data.source

import android.app.Application
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.project.therollamissionapp.data.*
import com.project.therollamissionapp.data.source.local.PatronDao
import com.project.therollamissionapp.ui.checkin.PatronListDiffCallback
import com.project.therollamissionapp.ui.common.Helpers.saveBitmap
import com.project.therollamissionapp.ui.common.ImageUtils.getPatronHeadshotPath
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import java.lang.Exception
import java.net.URL
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DefaultPatronRepository @Inject constructor (
    private val patronDao: PatronDao,
    private val retrofitService: ApiEndpointInterface,
    private val app: Application
) : PatronRepository {
    private val MIN_SEARCH_LENGTH = 3
    var currentQuery = 0
    val patrons = MutableLiveData<List<Patron>>()

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

    override fun getPatronSearchLiveData(): LiveData<List<Patron>> {
        return patrons
    }

    override fun getPatron(patron: Patron): LiveData<Patron> {
        return patronDao.getPatronById(patron.id)
    }

    override fun updateSearchString(name: String) {
        currentQuery += 1
        if (name.length < MIN_SEARCH_LENGTH) {
            patrons.postValue(emptyList())
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val localPatrons = patronDao.getPatronsByName("%${name}%")
                // Must copy toList() to prevent a race condition. If the reference to localPatrons
                // is altered by PatronListUpdateCallback while its live data value is being
                // posted, the list of patrons displayed to the user may not be accurate.
                patrons.postValue(localPatrons.toList())
                try {
                    val thisQuery = currentQuery
                    val call = retrofitService.searchPatrons(name)
                    val remotePatrons = call.execute().body()
                    if (remotePatrons != null && thisQuery == currentQuery) {
                        val updatedList = compareResults(localPatrons, remotePatrons)
                        patrons.postValue(updatedList)
                    }
                } catch (e: Exception) {
                    // TODO: log exception
                }
            }
        }
    }

    override suspend fun updateHeadshot(patron: Patron): Result<Unit> {
        val headshotPart = getMultipartFileData("headshot", getPatronHeadshotPath(app, patron.id))
        val call = retrofitService.putHeadshot(patron.id, headshotPart)
        return sendCall(call)
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
        } catch (e: Exception) {
            return Result.Error(Exception(e.toString()))
        }
    }

    private fun getMultipartFileData(field: String, absPath: String): MultipartBody.Part {
        val MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg")
        val file = File(absPath)
        val requestBody = RequestBody.create(MEDIA_TYPE_JPEG, file)
        return MultipartBody.Part.createFormData(field, file.name, requestBody)
    }

    private fun compareResults(old: MutableList<Patron>, new: MutableList<Patron>): List<Patron> {
        val diffCallback = PatronListDiffCallback(old, new)
        val updateCallback = PatronListUpdateCallback(old, new)
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(updateCallback)
        return new
    }

    inner class PatronListUpdateCallback(
        val old: MutableList<Patron>,
        val new: MutableList<Patron>
    ): ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            for (i in 1..count) {
                old.set(position, payload as Patron)
                CoroutineScope(Dispatchers.IO).launch {
                    patronDao.updatePatron(payload)
                }
            }
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            val temp = old.get(fromPosition)
            old.set(fromPosition, old.get(toPosition))
            old.set(toPosition, temp)
        }

        override fun onInserted(position: Int, count: Int) {
            old.addAll(position, new.subList(position, position + count))
            for (i in 0 until count) {
                CoroutineScope(Dispatchers.IO).launch {
                    attemptInsertPatron(new.get(position + i))
                }
            }
        }

        override fun onRemoved(position: Int, count: Int) {
            for (i in 1..count) {
                val patron = old.get(position)
                old.removeAt(position)
                CoroutineScope(Dispatchers.IO).launch {
                    deletePatron(patron)
                }
            }
        }
    }

    private fun attemptInsertPatron(patron: Patron) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = retrofitService.getHeadshot(patron.id)
            try {
                val result = call.execute()
                val url = URL(result.body()?.url)
                val headshot = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                val dir = app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (dir != null) {
                    saveBitmap(dir, "${patron.id}.jpg", headshot)
                    patronDao.insertPatron(patron)
                }
            } catch (e: Exception) {
                // TODO: log that we are unable to connect to the server
            }
        }
    }
}