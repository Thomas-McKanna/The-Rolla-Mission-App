package com.project.therollamissionapp.ui.checkin

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.therollamissionapp.Event
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.source.PatronRepository
import com.project.therollamissionapp.ui.common.DialogUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckInViewModel @Inject constructor(
    private val patronRepository: PatronRepository
) : ViewModel() {
    val query = MutableLiveData<String>()

    private val _patrons = MutableLiveData<List<Patron>>()
    val patrons: LiveData<List<Patron>> = _patrons

    private val _cancelEvent = MutableLiveData<Event<Unit>>()
    val cancelEvent: LiveData<Event<Unit>> = _cancelEvent

    private val _loadingStatus = MutableLiveData<Result<Unit>>()
    val loadingStatus: LiveData<Result<Unit>> = _loadingStatus

    private val _patronCheckInEvent = MutableLiveData<Event<Unit>>()
    val patronCheckInEvent: LiveData<Event<Unit>> = _patronCheckInEvent

    private val _checkInErrorEvent = MutableLiveData<Event<Unit>>()
    val checkInErrorEvent: LiveData<Event<Unit>> = _checkInErrorEvent

    lateinit var lastAttemptedCheckIn: Patron

    fun onQueryChanged() {
        _loadingStatus.value = Result.Loading
        query.value?.apply query@ {
            viewModelScope.launch {
                val result = patronRepository.getPatronsWithName(this@query)
                if (result is Result.Success) {
                    _patrons.value = result.data
                }
                _loadingStatus.value = null
            }
        }
    }

    fun cancelCheckIn() {
        _cancelEvent.value = Event(Unit)
    }

    fun checkIn(patron: Patron) {
        _loadingStatus.value = Result.Loading
        viewModelScope.launch {
            val result = patronRepository.checkIn(patron)
            _loadingStatus.value = null
            if (result is Result.Success) {
                _patronCheckInEvent.value = Event(Unit)
            } else {
                lastAttemptedCheckIn = patron
                _checkInErrorEvent.value = Event(Unit)
            }
        }
    }

    fun getErrorDialogue(context: Context?): AlertDialog? {
        return DialogUtil.makeErrorDialogue(context,
            onPositive = DialogInterface.OnClickListener() {dialog, id ->
                lastAttemptedCheckIn?.apply { checkIn(this) }
            },
            onNegative = null
        )
    }
}