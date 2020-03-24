package com.project.therollamissionapp.ui.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.therollamissionapp.CoroutineTestRule
import com.project.therollamissionapp.util.FakePatronRepository
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.not
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistrationViewModelTest {
    private lateinit var registrationViewModel: RegistrationViewModel

    @Before
    fun setupViewModel() {
        val repository = FakePatronRepository()
        registrationViewModel = RegistrationViewModel(repository)
    }

    @get:Rule
    var instanceExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun takeImage_setTakeImageEvent() {
        registrationViewModel.takeImage()
        val value = registrationViewModel.takeImageEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), `is`(not(nullValue())))
    }

    @Test
    fun setImageUri_toMyUri_returnMyUri() {
        registrationViewModel.setImageUri("myUri")
        val value = registrationViewModel.imagePath.getOrAwaitValue()
        assertThat(value, `is`("myUri"))
    }

    private fun fillRegistrationViewModel(viewModel: RegistrationViewModel) {
        viewModel.name.value = ""
        viewModel.contactNumber.value = ""
        viewModel.gender.value = "Male"
        viewModel.birthDate.value = "01/02/2020"
        viewModel.city.value = ""
        viewModel.violence.value = 0
        viewModel.veteran.value = 0
        viewModel.sexOffender.value = 0
        viewModel.setImageUri("")
        viewModel.consent1.value = true
        viewModel.consent2.value = true
        viewModel.consent3.value = true
    }
}