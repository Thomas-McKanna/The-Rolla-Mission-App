package com.project.therollamissionapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.therollamissionapp.data.CheckIn
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.PatronWithCheckIns

@Dao
interface PatronDao {
    @Query("SELECT * FROM patrons WHERE patronId = :id")
    fun getPatronById(id: String): LiveData<Patron>

    // If you want to use wildcards, you must put them in the name parameter itself.
    @Query("SELECT * FROM patrons WHERE firstName || ' ' || lastName LIKE :name")
    fun getPatronsByName(name: String): LiveData<List<Patron>>

    @Query("SELECT * FROM patrons")
    fun getPatrons(): LiveData<List<Patron>>

    @Transaction
    @Query("SELECT * FROM patrons")
    fun getPatronsWithCheckIns(): List<PatronWithCheckIns>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPatron(patron: Patron)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckIn)
}