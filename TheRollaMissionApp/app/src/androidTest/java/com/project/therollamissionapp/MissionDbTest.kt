package com.project.therollamissionapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.therollamissionapp.data.CheckIn
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.source.local.MissionDb
import com.project.therollamissionapp.data.source.local.PatronDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MissionDbTest {
    private lateinit var patronDao: PatronDao
    private lateinit var db: MissionDb

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MissionDb::class.java).build()
        patronDao = db.patronDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertPatron_findsPatronById() = runBlockingTest {
        val patron: Patron = TestUtil.makePatron()
        val id = patron.id

        patronDao.insertPatron(patron)

        val value = patronDao.getPatronById(id).getOrAwaitValue()
        assertThat(value.id, `is`(id))
    }

    @Test
    fun insertPatron_returnsAllPatrons() = runBlockingTest {
        val numPatrons = 10
        for (x in 0 until numPatrons) {
            patronDao.insertPatron(TestUtil.makePatron())
        }

        val patrons = patronDao.getPatrons().getOrAwaitValue()
        assertThat(patrons.size, `is`(numPatrons))
    }

    @Test
    fun insertPatron_findsPatronByName() = runBlockingTest {
        val patron1: Patron = Patron(firstName = "a", lastName = "")
        val patron2: Patron = Patron(firstName = "ab", lastName = "c")
        val patron3: Patron = Patron(firstName = "ab", lastName = "cd")

        patronDao.insertPatron(patron1)
        patronDao.insertPatron(patron2)
        patronDao.insertPatron(patron3)

        var patrons = patronDao.getPatronsByName("a%").getOrAwaitValue()
        assertThat(patrons.size, `is`(3)) // should match all 3
        patrons = patronDao.getPatronsByName("ab%").getOrAwaitValue()
        assertThat(patrons.size, `is`(2)) // should match 2
        patrons = patronDao.getPatronsByName("ab cd%").getOrAwaitValue()
        assertThat(patrons.size, `is`(1)) // should match 1
    }

    @Test
    fun insertCheckIn_findsCheckInsForPatron() = runBlockingTest {
        val patron = TestUtil.makePatron()
        val checkIn = CheckIn(patron.id)

        patronDao.insertPatron(patron)
        patronDao.insertCheckIn(checkIn)

        val value = patronDao.getPatronsWithCheckIns()
        assertThat(value.size, `is`(1))
        assertThat(value.get(0).checkIns.size, `is`(1))
    }
}