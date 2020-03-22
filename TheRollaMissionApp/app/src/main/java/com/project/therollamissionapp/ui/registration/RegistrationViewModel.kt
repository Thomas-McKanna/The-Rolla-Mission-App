package com.project.therollamissionapp.ui.registration

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.github.gcacace.signaturepad.views.SignaturePad
import com.project.therollamissionapp.Event
import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.source.PatronRepository
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.ui.common.DialogUtil
import com.project.therollamissionapp.ui.common.Helpers
import com.project.therollamissionapp.ui.registration.IdMappings.getOffender
import com.project.therollamissionapp.ui.registration.IdMappings.getVeteran
import com.project.therollamissionapp.ui.registration.IdMappings.getViolence
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val patronRepository: PatronRepository
): ViewModel() {
    val id = UUID.randomUUID().toString()

    val name = MutableLiveData<String>()
    val contactNumber = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val birthDate = MutableLiveData<String>()

    val city = MutableLiveData<String>()
    val reason = MutableLiveData<String>()
    val otherReason = MutableLiveData<String>()

    val timeHomeless = MutableLiveData<String>()
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

    private val _takeImageEvent = MutableLiveData<Event<String>>()
    val takeImageEvent: LiveData<Event<String>> = _takeImageEvent

    private val _verifyPatronEvent = MutableLiveData<Event<Unit>>()
    val verifyPatronEvent: LiveData<Event<Unit>> = _verifyPatronEvent

    private val _patronCreatedEvent = MutableLiveData<Event<Unit>>()
    val patronCreatedEvent: LiveData<Event<Unit>> = _patronCreatedEvent

    private val _contentChangedEvent = MutableLiveData<Event<Int>>()
    val contentChangedEvent: LiveData<Event<Int>> = _contentChangedEvent

    private val _registrationCanceledEvent = MutableLiveData<Event<Unit>>()
    val registrationCanceledEvent: LiveData<Event<Unit>> = _registrationCanceledEvent

    private val _hideKeyboardEvent = MutableLiveData<Event<Unit>>()
    val hideKeyboardEvent: LiveData<Event<Unit>> = _hideKeyboardEvent

    private val _uploadPatronResult = MutableLiveData<Result<Unit>>()
    val uploadPatronResult: LiveData<Result<Unit>> = _uploadPatronResult

    private val _errorDialogEvent = MutableLiveData<Event<Unit>>()
    val errorDialogEvent: LiveData<Event<Unit>> = _errorDialogEvent

    private val _warningDialogEvent = MutableLiveData<Event<Unit>>()
    val warningDialogEvent: LiveData<Event<Unit>> = _warningDialogEvent

    private val _successDialogEvent = MutableLiveData<Event<Unit>>()
    val successDialogEvent: LiveData<Event<Unit>> = _successDialogEvent

    fun backPressed() {
        _warningDialogEvent.value = Event(Unit)
    }

    fun submitPressed() {
        if (_uploadPatronResult.value == null || _uploadPatronResult.value is Result.Error) {
            _verifyPatronEvent.value = Event(Unit)
        }
    }

    fun takeImage() {
        _takeImageEvent.value = Event(id)
    }

    fun setImageUri(stringUri: String) {
        _imageUri.value = stringUri
    }

    fun onSignedSignature(sp: SignaturePad) {
        _signature.value = sp.signatureBitmap
    }

    fun triggerHideKeyboardEvent() {
        _hideKeyboardEvent.value = Event(Unit)
    }

    fun getErrorDialog(context: Context): AlertDialog? {
        return DialogUtil.makeErrorDialog(context,
            onPositive = DialogInterface.OnClickListener { dialog, id ->
                savePatron(context)
            },
            onNegative = DialogInterface.OnClickListener { dialog, id ->
                _registrationCanceledEvent.value = Event(Unit)
            })
    }

    fun getWarningDialog(context: Context?): AlertDialog? {
        return DialogUtil.makeWarningDialog(context,
            onPositive = DialogInterface.OnClickListener { dialog, id ->
                _registrationCanceledEvent.value = Event(Unit)
            },
            onNegative = null
        )
    }

    fun getSuccessDialog(context: Context?): AlertDialog? {
        return DialogUtil.makeSuccessDialog(context,
            onPositive = DialogInterface.OnClickListener { dialog, id ->
                _patronCreatedEvent.value = Event(Unit)
            },
            onNegative = DialogInterface.OnClickListener { dialog, id ->
                // Not actually a cancellation, but the effect of returning to start is achieved
                _registrationCanceledEvent.value = Event(Unit)
            }
        )
    }

    fun pictureTaken(): Boolean {
        return _imageUri.value != null
    }

    fun hasSigned(): Boolean {
        return _signature.value != null
    }

    fun savePatron(context: Context) {
        val date_homeless = Helpers.getDateHomeless(context, timeHomeless.value)
        val patron = ExtendedPatron(
            name = name.value ?: "",
            birth_date = birthDate.value ?: "",
            gender = gender.value ?: "",
            phone = contactNumber.value ?: "",
            city = city.value ?: "",
            reason = reason.value ?: "",
            time_homeless = date_homeless,
            veteran = getVeteran(veteran.value ?: 0),
            violence = getViolence(violence.value ?: 0),
            offender = getOffender((sexOffender.value ?: 0)),
            id = id
        )
        viewModelScope.launch {
            _uploadPatronResult.value = Result.Loading
            val result = patronRepository.insertPatron(patron)
            _uploadPatronResult.value = result
            if (result is Result.Success) {
                _successDialogEvent.value = Event(Unit)
            } else {
                _errorDialogEvent.value = Event(Unit)
            }
        }
    }
}
