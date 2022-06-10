package com.mobile.mobiletradermtx.ui.messages

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.EntityAccuracy
import com.mobile.mobiletradermtx.dto.toAccuracyEntity
import com.mobile.mobiletradermtx.ui.messages.repository.MessageRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MessageViewModel @ViewModelInject constructor(private val repo: MessageRepo) : ViewModel() {

    private val _messageResponseState =
        MutableStateFlow<NetworkResult<List<EntityAccuracy>>>(NetworkResult.Empty)
    val messageResponseState get() = _messageResponseState

    fun isMessageAccuracy(customerCode: String, entriesDate: String) = viewModelScope.launch {
        _messageResponseState.value = NetworkResult.Loading
        try {
            Log.d("EPOKHAI 0","$customerCode $entriesDate")
            val isDataAccuracy = repo.getDataAccuracy()
            val entryDate = isDataAccuracy.filter { it.entry_date.equals(entriesDate) }
            Log.d("EPOKHAI 1","$entryDate")

            if (entryDate.isNotEmpty()) {
                _messageResponseState.value = NetworkResult.Success(isDataAccuracy)
                Log.d("EPOKHAI 2","")
            } else {
                Log.d("EPOKHAI 3","$entryDate")
                val isData = repo.dataAccuracy(customerCode)
                Log.d("EPOKHAI 4","$isData")
                if (isData.status == 200 || isData.accuracy!!.isNotEmpty()) {
                    Log.d("EPOKHAI 5","")
                    val result = isData.accuracy!!.map { it.toAccuracyEntity() }
                    repo.isCurrentMessage(result)
                    _messageResponseState.value = NetworkResult.Success(repo.getDataAccuracy())
                } else {
                    Log.d("EPOKHAI 6","")
                    _messageResponseState.value = NetworkResult.Success(emptyList())
                }
            }
        } catch (e: Throwable) {
                Log.d("EPOKHAI 7","${e.message}")
            _messageResponseState.value = NetworkResult.Error(e)
        }
    }


    /**
     * Update Status icon at on click.
     * @Update Update Status icon at on click
     */
    fun isMessageUpdateAccuracy(status: Int, id: String) = viewModelScope.launch {
        try {
            NetworkResult.Success(repo.updateDataAccuracyStatus(status, id))
        } catch (e: Throwable) {}
    }

    //put it on modules.
    /**
     *  Status count as notification.
     * @SelectCount change message status from unread to read.....
     */
    private val _countResponseState = MutableStateFlow<NetworkResult<Int>>(NetworkResult.Empty)
    val countResponseState get() = _countResponseState

    fun isMessageCountAccuracy() = viewModelScope.launch {
        try {
            _countResponseState.value =
                NetworkResult.Success(repo.getCountDataAccuracyStatus())
        } catch (e: Throwable) {
            _countResponseState.value = NetworkResult.Error(e)
        }
    }


}