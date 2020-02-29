package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.source.local.PatronDao
import kotlinx.coroutines.*
import javax.inject.Inject

class DefaultPatronRepository (
    private val patronDao: PatronDao,
    private val ioDispatcher: CoroutineDispatcher
) : PatronRepository {
    @Inject
    constructor(patronDao: PatronDao) : this(patronDao, Dispatchers.IO)

    override suspend fun insertPatron(extendedPatron: ExtendedPatron): Result<Unit> {
        delay(3000) // TODO: currently simulating upload time to demonstrate progress bar
        withContext(ioDispatcher) {
            // TODO:
            // (1) Attempt to upload ExtendedPatron to external web service.
            // (2) If successful, insert Patron into local db and return Success
            // (3) If fails, return Failure.
            val patron = Patron(
                extendedPatron.firstName,
                extendedPatron.lastName,
                extendedPatron.id
            )
            patronDao.insertPatron(patron)
        }
        return Result.Success<Unit>(Unit)
    }

    override suspend fun insertPatron(patron: Patron) {
        patronDao.insertPatron(patron)
    }

    override suspend fun checkIn(patron: Patron): Result<Unit> {
        // TODO: Attempt to upload checkin instance to external web service.
        return Result.Success(Unit)
    }

    override suspend fun deletePatron(patron: Patron) {
        withContext(ioDispatcher) {
            launch { patronDao.deletePatron(patron) }
        }
    }
}