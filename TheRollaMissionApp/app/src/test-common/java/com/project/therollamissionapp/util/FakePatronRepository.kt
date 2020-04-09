package com.project.therollamissionapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    override suspend fun deletePatron(patron: Patron) {
        // NOT IMPLEMENTED
    }

    override fun getPatronSearchLiveData(): LiveData<List<Patron>> {
        // NOT IMPLEMENTED
        val list: List<Patron> = emptyList()
        return MutableLiveData<List<Patron>>(list)
    }

    override fun updateSearchString(name: String) {
        // NOT IMPLEMENTED
    }

    override fun getPatron(patron: Patron): LiveData<Patron> {
        // NOT IMPLEMENTED
        return MutableLiveData<Patron>(Patron())
    }

    override suspend fun updateHeadshot(patron: Patron): Result<Unit> {
        // NOT IMPLEMENTED
        return Result.Success(Unit)
    }
}