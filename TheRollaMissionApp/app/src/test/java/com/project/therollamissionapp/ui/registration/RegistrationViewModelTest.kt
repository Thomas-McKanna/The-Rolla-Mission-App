package com.project.therollamissionapp.ui.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.therollamissionapp.CoroutineTestRule
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.source.FakePatronRepository
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
    fun backPressed_indexZero_FirstSectionShown() {
        registrationViewModel.backPressed()
        val value = registrationViewModel.contentChangedEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), `is`(R.layout.reg_part1))

    }

    @ExperimentalCoroutinesApi
    @Test
    fun nextPressed_indexMax_LastSectionShown() {
        fillRegistrationViewModel(registrationViewModel)
        for (x in 0..5) registrationViewModel.nextPressed()
        val value = registrationViewModel.contentChangedEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), `is`(R.layout.reg_part5))
    }

    @Test
    fun nextPressed_notFilledOut_setSnackbarEvent() {
        registrationViewModel.nextPressed()
        val value = registrationViewModel.snackbarText.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), `is`(not(nullValue())))
    }

    @Test
    fun startBirthDateDialogue_setsShowDatePickerEvent() {
        registrationViewModel.startBirthDateDialogue()
        val value = registrationViewModel.birthDateDialogueEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), `is`(not(nullValue())))
    }

    @Test
    fun setBirthDate_Feb172020_setTo02172020() {
        // DatePicker results zero-indexed months
        registrationViewModel.setBirthDate(2020, 2 - 1, 17)
        val value = registrationViewModel.birthDate.getOrAwaitValue()
        assertThat(value, `is`("2/17/2020"))
    }

    @Test
    fun takeImage_setTakeImageEvent() {
        registrationViewModel.takeImage()
        val value = registrationViewModel.takeImageEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), `is`(not(nullValue())))
    }

    @Test
    fun setImageUri_toMyUri_returnMyUri() {
        registrationViewModel.setImageUri("myUri")
        val value = registrationViewModel.imageUri.getOrAwaitValue()
        assertThat(value, `is`("myUri"))
    }

    private fun fillRegistrationViewModel(viewModel: RegistrationViewModel) {
        viewModel.firstName.value = ""
        viewModel.lastName.value = ""
        viewModel.contactNumber.value = ""
        viewModel.gender.value = 0
        viewModel.setBirthDate(0, 0, 0)
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