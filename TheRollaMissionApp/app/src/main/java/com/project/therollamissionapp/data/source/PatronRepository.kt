package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.Patron
import javax.inject.Inject

class PatronRepository @Inject constructor(

) {
    suspend fun getPatrons(forceUpdate: Boolean = false): Result<List<Patron>> {
        return Result.Success(emptyList())
    }

    suspend fun refreshPatrons() {}

    suspend fun getPatron(patronId: String, forceUpdate: Boolean = false): Result<Patron> {
        return Result.Success(Patron())
    }

    suspend fun refreshPatron(patronId: String) {}

    suspend fun savePatron(patron: Patron) {}

    suspend fun deletePatron(patronId: String) {}
}