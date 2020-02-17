package com.project.therollamissionapp.ui.registration

import android.net.Uri
import androidx.lifecycle.*
import com.project.therollamissionapp.Event
import com.project.therollamissionapp.R
import com.project.therollamissionapp.repository.patronRepository
import com.project.therollamissionapp.vo.Patron
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
        // TODO: based on the passed in index, check whether the appropriate fields have been filled
        return true
    }
}
