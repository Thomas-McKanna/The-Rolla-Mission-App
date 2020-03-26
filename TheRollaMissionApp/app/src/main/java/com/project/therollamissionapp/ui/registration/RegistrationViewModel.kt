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
import com.project.therollamissionapp.ui.common.Helpers.formatBirthDate
import com.project.therollamissionapp.ui.common.Helpers.formatDateHomeless
import com.project.therollamissionapp.ui.common.Helpers.saveBitmap
import com.project.therollamissionapp.ui.registration.IdMappings.getOffender
import com.project.therollamissionapp.ui.registration.IdMappings.getVeteran
import com.project.therollamissionapp.ui.registration.IdMappings.getViolence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    val timeHomeless = MutableLiveData<String>()
    val violence = MutableLiveData<Int>()
    val veteran = MutableLiveData<Int>()
    val sexOffender = MutableLiveData<Int>()

    private val _imagePath = MutableLiveData<String>()
    val imagePath: LiveData<String> = _imagePath

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
        triggerHideKeyboardEvent()
        if (_uploadPatronResult.value == null || _uploadPatronResult.value is Result.Error) {
            _verifyPatronEvent.value = Event(Unit)
        }
    }

    fun takeImage() {
        _takeImageEvent.value = Event(id)
    }

    fun setImageUri(stringUri: String) {
        _imagePath.value = stringUri
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
                // If the dialog has not been dismissed completely, the search view will fail
                // to show the keyboard upon start. Using the onDismiss callback of the dialog
                // produced unpredictable results. This solution gives the dialog time to close
                // before triggering the patron created event.
                CoroutineScope(Dispatchers.IO).launch {
                    delay(100)
                    _patronCreatedEvent.postValue(Event(Unit))
                }
            },
            onNegative = DialogInterface.OnClickListener { dialog, id ->
                // Not actually a cancellation, but the effect of returning to start is achieved
                _registrationCanceledEvent.value = Event(Unit)
            }
        )
    }

    fun pictureTaken(): Boolean {
        return _imagePath.value != null
    }

    fun hasSigned(): Boolean {
        return _signature.value != null
    }

    fun savePatron(context: Context) {
        val patron = constructPatronFromViewData(context)
        viewModelScope.launch {
            _uploadPatronResult.value = Result.Loading
            val result = patronRepository.createPatron(patron)
            _uploadPatronResult.value = result
            if (result is Result.Success) {
                _successDialogEvent.value = Event(Unit)
            } else {
                _errorDialogEvent.value = Event(Unit)
            }
        }
    }

    fun constructPatronFromViewData(context: Context): ExtendedPatron {
        val formattedDateHomeless = formatDateHomeless(context, timeHomeless.value)
        val formattedBirthDate = formatBirthDate(birthDate.value)
        val signatureFile = saveBitmap(context.cacheDir, "${id}signature.jpg", signature.value)
        val signaturePath = signatureFile.absolutePath
        return ExtendedPatron(
            id = id,
            name = name.value ?: "",
            birth_date = formattedBirthDate,
            gender = gender.value ?: "",
            phone = contactNumber.value ?: "",
            city = city.value ?: "Rolla",
            reason = reason.value ?: "",
            date_homeless = formattedDateHomeless,
            veteran = getVeteran(veteran.value ?: 0),
            violence = getViolence(violence.value ?: 0),
            offender = getOffender((sexOffender.value ?: 0)),
            headshotPath = _imagePath.value ?: "",
            signaturePath = signaturePath
        )
    }
}
