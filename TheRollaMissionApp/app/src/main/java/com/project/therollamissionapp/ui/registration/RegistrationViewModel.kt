package com.project.therollamissionapp.ui.registration

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.github.gcacace.signaturepad.views.SignaturePad
import com.project.therollamissionapp.Event
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.source.PatronRepository
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.ui.registration.ResIdMapping.getGender
import com.project.therollamissionapp.ui.registration.ResIdMapping.getOffender
import com.project.therollamissionapp.ui.registration.ResIdMapping.getReasonRolla
import com.project.therollamissionapp.ui.registration.ResIdMapping.getTimeHomeless
import com.project.therollamissionapp.ui.registration.ResIdMapping.getVeteran
import com.project.therollamissionapp.ui.registration.ResIdMapping.getViolence
import kotlinx.coroutines.launch
import java.util.*
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

    private val _title = MutableLiveData<String>().apply { postValue(getTitle(index)) }
    val title: LiveData<String> = _title

    val id = UUID.randomUUID().toString()

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

    private val _signature = MutableLiveData<Bitmap>()
    val signature: LiveData<Bitmap> = _signature

    private val _showDatePickerEvent = MutableLiveData<Event<Unit>>()
    val showDatePickerEvent: LiveData<Event<Unit>> = _showDatePickerEvent

    private val _takeImageEvent = MutableLiveData<Event<String>>()
    val takeImageEvent: LiveData<Event<String>> = _takeImageEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _patronCreatedEvent = MutableLiveData<Event<Unit>>()
    val patronCreatedEvent: LiveData<Event<Unit>> = _patronCreatedEvent

    private val _contentChangedEvent = MutableLiveData<Event<Int>>()
    val contentChangedEvent: LiveData<Event<Int>> = _contentChangedEvent

    private val _birthDateDialogueEvent = MutableLiveData<Event<DatePickerFragment>>()
    val birthDateDialogueEvent: LiveData<Event<DatePickerFragment>> = _birthDateDialogueEvent

    private val _hideKeyboardEvent = MutableLiveData<Event<Unit>>()
    val hideKeyboardEvent: LiveData<Event<Unit>> = _hideKeyboardEvent

    private val _uploadPatronResult = MutableLiveData<Result<Unit>>()
    val uploadPatronResult: LiveData<Result<Unit>> = _uploadPatronResult

    init {
        _contentChangedEvent.value = Event(sections.get(index))
    }

    fun backPressed() {
        if (index > 0) {
            index -= 1
        }
        _contentChangedEvent.value = Event(sections.get(index))
        triggerHideKeyboardEvent()
        _title.value = getTitle(index)
    }

    fun nextPressed() {
        if (fieldsFilledForIndex(index)) {
            index += 1
            if (index >= sections.size) {
                index = sections.size - 1 // prevent array out of bounds exceptions
                savePatron()
            } else {
                _contentChangedEvent.value = Event(sections.get(index))
            }
        } else {
            _snackbarText.value = Event(R.string.field_incomplete)
        }
        triggerHideKeyboardEvent()
        _title.value = getTitle(index)
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
        _takeImageEvent.value = Event(id)
    }

    fun setImageUri(stringUri: String) {
        setValueIfDifferent(_imageUri, stringUri)
    }

    fun onSignedSignature(sp: SignaturePad) {
        _signature.value = sp.signatureBitmap
    }

    fun triggerHideKeyboardEvent() {
        _hideKeyboardEvent.value = Event(Unit)
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
            imageUri = imageUri.value ?: "",
            id = id
        )
        viewModelScope.launch {
            _uploadPatronResult.value = Result.Loading
            val result = patronRepository.insertPatron(patron)
            _uploadPatronResult.value = result
            _patronCreatedEvent.value = Event(Unit)
        }
    }

    private fun fieldsFilledForIndex(index: Int): Boolean {
        return when (index) {
            0 -> firstName.value != null && lastName.value != null && contactNumber.value != null && birthDate.value != null && gender.value != 0
            1 -> city.value != null && reason.value != 0
            2 -> timeHomeless.value != 0 && violence.value != 0 && veteran.value != 0 && sexOffender.value != 0
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

    private fun getTitle(index: Int): String{
        return "Registration Part ${index + 1} of ${sections.size}"
    }
}
