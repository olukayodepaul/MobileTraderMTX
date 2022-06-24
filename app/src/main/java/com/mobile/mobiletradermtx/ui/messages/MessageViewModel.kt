package com.mobile.mobiletradermtx.ui.messages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.NotificationAndMessage
import com.mobile.mobiletradermtx.dto.toAccuracyEntity
import com.mobile.mobiletradermtx.ui.messages.repository.MessageRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MessageViewModel @ViewModelInject constructor(private val repo: MessageRepo) : ViewModel() {

    private val _messageResponseState =
        MutableStateFlow<NetworkResult<NotificationAndMessage>>(NetworkResult.Empty)
    val messageResponseState get() = _messageResponseState

    fun isMessageAccuracy(customerCode: String, entriesDate: String) = viewModelScope.launch {

        _messageResponseState.value = NetworkResult.Loading

        try {

            val isDataAccuracy = repo.getDataAccuracy()
            val entryDate = isDataAccuracy.filter { it.entry_date.equals(entriesDate) }

            val filterReadFromUnreadMessages = isDataAccuracy.count {
                it.status == 1
            }

            if (entryDate.isNotEmpty()) {

                val notificationAndMessage = NotificationAndMessage(
                    filterReadFromUnreadMessages,isDataAccuracy
                )

                _messageResponseState.value = NetworkResult.Success(notificationAndMessage)

            } else {

                val isData = repo.dataAccuracy(customerCode)

                if (isData.status == 200 || isData.accuracy!!.isNotEmpty()) {

                    val result = isData.accuracy!!.map { it.toAccuracyEntity() }
                    repo.isCurrentMessage(result) //save in local Repository
                    val isAccuracyMessage = repo.getDataAccuracy()

                    val filterReadFromUnreadMessage = isAccuracyMessage.count {
                        it.status == 1
                    }

                    val notificationAndMessage = NotificationAndMessage(
                        filterReadFromUnreadMessage, isAccuracyMessage
                    )

                    _messageResponseState.value = NetworkResult.Success(notificationAndMessage)
                } else {
                    val notificationAndMessage = NotificationAndMessage(
                        filterReadFromUnreadMessages,isDataAccuracy
                    )
                    _messageResponseState.value = NetworkResult.Success(notificationAndMessage)
                }
            }
        } catch (e: Throwable) {
            _messageResponseState.value = NetworkResult.Error(e)
        }
    }

    /**
     * Update Status icon at on click.
     * @Update Update Status icon at on click
     */
    fun isMessageUpdateAccuracy(status: Int, id: String, readStatus: Int) = viewModelScope.launch {
        try {
            if(readStatus==1) repo.messageNotify(id)
            NetworkResult.Success(repo.updateDataAccuracyStatus(status, id))
        } catch (e: Throwable) {

        }
    }

}