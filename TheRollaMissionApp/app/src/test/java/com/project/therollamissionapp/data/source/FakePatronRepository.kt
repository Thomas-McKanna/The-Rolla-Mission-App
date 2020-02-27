package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.Result

class FakePatronRepository : PatronRepository {
    override suspend fun insertPatron(extendedPatron: ExtendedPatron): Result<Unit> {
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

    override suspend fun deletePatron(patron: Patron) {
        // NOT IMPLEMENTED
    }
}