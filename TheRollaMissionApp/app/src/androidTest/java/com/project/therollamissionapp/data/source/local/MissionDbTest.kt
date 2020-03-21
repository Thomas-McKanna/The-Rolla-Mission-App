package com.project.therollamissionapp.data.source.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.therollamissionapp.util.MainCoroutineRule
import com.project.therollamissionapp.util.TestUtil
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
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
    var mainCoroutineRule =
        MainCoroutineRule()

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
        val patron: Patron =
            TestUtil.makePatron()
        val id = patron.id

        patronDao.insertPatron(patron)

        val value = patronDao.getPatronById(id).getOrAwaitValue()
        assertThat(value.id, `is`(id))
    }

    @Test
    fun insertPatron_findsPatronByName() = runBlockingTest {
        val patron1: Patron = Patron(name = "smith")
        val patron2: Patron = Patron(name = "michael")
        val patron3: Patron = Patron(name = "marry")

        patronDao.insertPatron(patron1)
        patronDao.insertPatron(patron2)
        patronDao.insertPatron(patron3)

        var patrons = patronDao.getPatronsByName("%m%")
        assertThat(patrons.size, `is`(3)) // should match all 3
        patrons = patronDao.getPatronsByName("%mi%")
        assertThat(patrons.size, `is`(2)) // should match 2
        patrons = patronDao.getPatronsByName("%smi%")
        assertThat(patrons.size, `is`(1)) // should match 1
    }

    @Test
    fun deletePatron_InsertTwoDeleteOne_OneRemains() = runBlockingTest {
        val patron1 = TestUtil.makePatron()
        val patron2 = TestUtil.makePatron()

        patronDao.insertPatron(patron1)
        patronDao.insertPatron(patron2)
        patronDao.deletePatron(patron1)

        var value = patronDao.getPatronById(patron1.id).getOrAwaitValue()
        assertThat(value, `is`(nullValue()))
        value = patronDao.getPatronById(patron2.id).getOrAwaitValue()
        assertThat(value.id, `is`(patron2.id))
    }
}