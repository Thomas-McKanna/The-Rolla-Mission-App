package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.Patron

interface PatronRepository {
    suspend fun createPatron(extendedPatron: ExtendedPatron) : Result<Unit>

    suspend fun insertPatron(patron: Patron)

    suspend fun getPatronsWithName(name: String): Result<List<Patron>>

    suspend fun checkIn(patron: Patron): Result<Unit>

    suspend fun deletePatron(patron: Patron)
}