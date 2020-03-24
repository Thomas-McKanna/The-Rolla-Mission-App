package com.project.therollamissionapp.util

import com.project.therollamissionapp.data.source.PatronRepository
import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.Result

class FakePatronRepository : PatronRepository {
    override suspend fun createPatron(extendedPatron: ExtendedPatron): Result<Unit> {
        // NOT IMPLEMENTED
        return Result.Success(Unit)
    }

    override suspend fun insertPatron(patron: Patron) {
        // NOT IMPLEMENTED
    }

    override suspend fun checkIn(patron: Patron): Result<Unit> {
        // NOT IMPLEMENTED
        return Result.Success(Unit)
    }

    override suspend fun getPatronsWithName(name: String): Result<List<Patron>> {
        // NOT IMPLEMENTED
        return Result.Success(emptyList())
    }

    override suspend fun deletePatron(patron: Patron) {
        // NOT IMPLEMENTED
    }
}