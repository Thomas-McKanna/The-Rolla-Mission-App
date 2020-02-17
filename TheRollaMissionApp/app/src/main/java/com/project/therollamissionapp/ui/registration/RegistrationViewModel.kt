package com.project.therollamissionapp.ui.registration

import android.net.Uri
import androidx.lifecycle.*
import com.project.therollamissionapp.Event
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.source.patronRepository
import com.project.therollamissionapp.data.Patron
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val patronRepository: patronRepository
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

    val violence = MutableLiveData<Int>()
    val veteran = MutableLiveData<Int>()
    val sex_offender = MutableLiveData<Int>()

    private val _image_uri = MutableLiveData<Uri>()
    val image_uri: LiveData<Uri> = _image_uri

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
            // set _snackbarText indicating what is missing
        }
    }

    fun showDatePicker() {
        _showDatePickerEvent.value = Event(Unit)
    }

    fun setBirthDate(year: Int, month: Int, day: Int) {
        val birthDate = String.format("%d/%d/%d", month, day, year)
        setValueIfDifferent(_birthDate, birthDate)
    }

    fun takeImage() {
        _takeImageEvent.value = Event(Unit)
    }

    fun setImageUri(imageUri: Uri) {
        setValueIfDifferent(_image_uri, imageUri)
    }

    private fun savePatron() {
        // TODO: compile all fields into a new Patron object
        val patron = Patron()
        viewModelScope.launch {
            patronRepository.savePatron(patron)
            _patronCreatedEvent.value = Event(Unit)
        }
    }

    private fun fieldsFilledForIndex(index: Int): Boolean {
        return true
        return when (index) {
            0 -> firstName.value != null && lastName.value != null && contactNumber.value != null && birthDate.value != null && gender.value != null
            1 -> city.value != null && reason.value != null
            2 -> violence.value != null && veteran.value != null && sex_offender.value != null
            3 -> image_uri.value != null
            4 -> consent1.value != null && consent2.value != null && consent3.value != null
            else -> true
        }
    }

    private fun <T> setValueIfDifferent(ld: MutableLiveData<T>, value: T) {
        if (ld.value != value) {
            ld.value = value
        }
    }
}
