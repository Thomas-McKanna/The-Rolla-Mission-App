package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.Result

class FakePatronRepository : PatronRepository {
    override suspend fun insertPatron(extendedPatron: ExtendedPatron): Result<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insertPatron(patron: Patron) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun checkIn(patron: Patron) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deletePatron(patron: Patron) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}