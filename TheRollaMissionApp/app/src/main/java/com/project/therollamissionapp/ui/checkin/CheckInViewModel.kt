package com.project.therollamissionapp.ui.checkin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.therollamissionapp.Event
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.source.PatronRepository
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
}