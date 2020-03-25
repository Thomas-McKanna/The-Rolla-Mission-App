package com.project.therollamissionapp.data.source

import androidx.lifecycle.LiveData
import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.Patron

interface PatronRepository {
    suspend fun createPatron(extendedPatron: ExtendedPatron) : Result<Unit>

    suspend fun insertPatron(patron: Patron)

    fun getPatronSearchLiveData(): LiveData<List<Patron>>

    fun updateSearchString(name: String)

    suspend fun checkIn(patron: Patron): Result<Unit>

    suspend fun deletePatron(patron: Patron)
}