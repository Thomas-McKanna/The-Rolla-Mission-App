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

    private val _birthDate = MutableLiveData<String>()
    val birthDate: LiveData<String> = _birthDate

    val contactNumber = MutableLiveData<String>()

    private val _gender = MutableLiveData<Int>()
    val gender: LiveData<Int> = _gender

    val city = MutableLiveData<String>()

    private val _reason = MutableLiveData<Int>()
    val reason: LiveData<Int> = _reason

    val otherReason = MutableLiveData<String>()

    private val _violence = MutableLiveData<Int>()
    val violence: LiveData<Int> = _violence

    private val _veteran = MutableLiveData<Int>()
    val veteran: LiveData<Int> = _veteran

    private val _sex_offender = MutableLiveData<Int>()
    val sex_offender = _sex_offender

    private val _image_uri = MutableLiveData<Uri>()
    val image_uri: LiveData<Uri> = _image_uri

    private val _consent1 = MutableLiveData<Boolean>()
    val consent1: LiveData<Boolean> = _consent1

    private val _consent2 = MutableLiveData<Boolean>()
    val consent2: LiveData<Boolean> = _consent2

    private val _consent3 = MutableLiveData<Boolean>()
    val consent3: LiveData<Boolean> = _consent3

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _patronCreatedEvent = MutableLiveData<Event<Unit>>()
    val patronCreatedEvent: LiveData<Event<Unit>> = _patronCreatedEvent

    private val _contentChangedEvent = MutableLiveData<Event<Int>>()
    val contentChangedEvent: LiveData<Event<Int>> = _contentChangedEvent

    fun backPressed() {
        if (index > 0) {
            index -= 1
        }
        _contentChangedEvent.value = Event(sections.get(index))
    }

    fun nextPressed() {
        if (fieldsFilledForIndex(index)) {
            if (index < sections.size - 1) {
                index += 1
            }
            _contentChangedEvent.value = Event(sections.get(index))
        } else {
            // set _snackbarText indicating what is missing
        }
    }

    private fun savePatron(patron: Patron) {
        // TODO: compile all fields into a new Patron object
        viewModelScope.launch {
            patronRepository.savePatron(patron)
            _patronCreatedEvent.value = Event(Unit)
        }
    }

    private fun fieldsFilledForIndex(index: Int): Boolean {
        return when (index) {
            0 -> firstName.value != null && lastName.value != null && contactNumber.value != null && birthDate.value != null && gender.value != null
            1 -> city.value != null && reason.value != null
            2 -> violence.value != null && veteran.value != null && sex_offender.value != null
            3 -> image_uri.value != null
            4 -> consent1.value != null && consent2.value != null && consent3.value != null
            else -> true
        }
    }
}
