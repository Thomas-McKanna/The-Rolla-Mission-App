package com.project.therollamissionapp.ui.registration

import androidx.lifecycle.*
import com.project.therollamissionapp.Event
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.source.PatronRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val patronRepository: PatronRepository
): ViewModel() {
    private var index = 0
    private val sections = listOf<Int>(
        R.layout.reg_part1,
        R.layout.reg_part2,
        R.layout.reg_part3,
        R.layout.reg_part4,
        R.layout.reg_part5
    )

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val contactNumber = MutableLiveData<String>()
    val gender = MutableLiveData<Int>()
    private val _birthDate = MutableLiveData<String>()
    val birthDate: LiveData<String> = _birthDate

    val city = MutableLiveData<String>()
    val reason = MutableLiveData<Int>()
    val otherReason = MutableLiveData<String>()

    val timeHomeless = MutableLiveData<Int>()
    val violence = MutableLiveData<Int>()
    val veteran = MutableLiveData<Int>()
    val sexOffender = MutableLiveData<Int>()

    private val _imageUri = MutableLiveData<String>()
    val imageUri: LiveData<String> = _imageUri

    val consent1 = MutableLiveData<Boolean>()
    val consent2 = MutableLiveData<Boolean>()
    val consent3 = MutableLiveData<Boolean>()

    private val _showDatePickerEvent = MutableLiveData<Event<Unit>>()
    val showDatePickerEvent: LiveData<Event<Unit>> = _showDatePickerEvent

    private val _takeImageEvent = MutableLiveData<Event<Unit>>()
    val takeImageEvent: LiveData<Event<Unit>> = _takeImageEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _patronCreatedEvent = MutableLiveData<Event<Unit>>()
    val patronCreatedEvent: LiveData<Event<Unit>> = _patronCreatedEvent

    private val _contentChangedEvent = MutableLiveData<Event<Int>>()
    val contentChangedEvent: LiveData<Event<Int>> = _contentChangedEvent

    private val _birthDateDialogueEvent = MutableLiveData<Event<DatePickerFragment>>()
    val birthDateDialogueEvent: LiveData<Event<DatePickerFragment>> = _birthDateDialogueEvent

    init {
        _contentChangedEvent.value = Event(sections.get(index))
    }

    fun backPressed() {
        if (index > 0) {
            index -= 1
        }
        _contentChangedEvent.value = Event(sections.get(index))
    }

    fun nextPressed() {
        if (fieldsFilledForIndex(index)) {
            index += 1
            if (index >= sections.size) {
                index = sections.size - 1 // prevent array out of bounds exceptions
                savePatron()
                _patronCreatedEvent.value = Event(Unit)
            } else {
                _contentChangedEvent.value = Event(sections.get(index))
            }
        } else {
            _snackbarText.value = Event(R.string.field_incomplete)
        }
    }

    fun showDatePicker() {
        _showDatePickerEvent.value = Event(Unit)
    }

    fun startBirthDateDialogue() {
        val newFragment = DatePickerFragment(this)
        _birthDateDialogueEvent.value = Event(newFragment)
    }

    fun setBirthDate(year: Int, month: Int, day: Int) {
        val birthDate = String.format("%d/%d/%d", month + 1, day, year)
        setValueIfDifferent(_birthDate, birthDate)
    }

    fun takeImage() {
        _takeImageEvent.value = Event(Unit)
    }

    fun setImageUri(stringUri: String) {
        setValueIfDifferent(_imageUri, stringUri)
    }

    private fun savePatron() {
        val patron = ExtendedPatron(
            firstName = firstName.value ?: "",
            lastName = lastName.value ?: "",
            dob = birthDate.value ?: "",
            gender = getGender(gender.value ?: 0),
            phone = contactNumber.value ?: "",
            cityWhenBecameHomeless = city.value ?: "",
            reasonRolla = getReasonRolla(reason.value ?: 0),
            otherReason = otherReason.value ?: "",
            timeHomeless = getTimeHomeless(timeHomeless.value ?: 0),
            veteran = getVeteran(veteran.value ?: 0),
            fleeingViolence = getViolence(violence.value ?: 0),
            sexOffender = getOffender((sexOffender.value ?: 0)),
            imageUri = imageUri.value ?: ""
        )
        viewModelScope.launch {
            patronRepository.insertPatron(patron)
            _patronCreatedEvent.value = Event(Unit)
        }
    }

    private fun fieldsFilledForIndex(index: Int): Boolean {
        return when (index) {
            0 -> firstName.value != null && lastName.value != null && contactNumber.value != null && birthDate.value != null && gender.value != null
            1 -> city.value != null
            2 -> timeHomeless.value != null || violence.value != null && veteran.value != null && sexOffender.value != null
            3 -> imageUri.value != null
            4 -> consent1.value != null && consent2.value != null && consent3.value != null
            else -> true
        }
    }

    private fun <T> setValueIfDifferent(ld: MutableLiveData<T>, value: T) {
        if (ld.value != value) {
            ld.value = value
        }
    }

    // TODO: all of these functions do not need to be in this class

    private fun getGender(id: Int): String {
        return when (id) {
            else -> "TODO"
        }
    }

    private fun getReasonRolla(id: Int): String {
        return when (id) {
            else -> "TODO"
        }
    }

    private fun getTimeHomeless(id: Int): String {
        return when (id) {
            else -> "TODO"
        }
    }

    private fun getVeteran(id: Int): Boolean {
        return when (id) {
            else -> false // TODO
        }
    }

    private fun getViolence(id: Int): Boolean {
        return when (id) {
            else -> false // TODO
        }
    }

    private fun getOffender(id: Int): Boolean {
        return when (id) {
            else -> false // TODO
        }
    }
}
