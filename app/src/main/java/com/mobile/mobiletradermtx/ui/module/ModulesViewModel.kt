package com.mobile.mobiletradermtx.ui.module


import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.ModulesResponse
import com.mobile.mobiletradermtx.dto.NotificationAndMessage
import com.mobile.mobiletradermtx.dto.toAccuracyEntity
import com.mobile.mobiletradermtx.ui.module.repository.ModulesRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ModulesViewModel @ViewModelInject constructor(private val repo: ModulesRepo): ViewModel() {

    private val _userModulesResponseState = MutableStateFlow<NetworkResult<ModulesResponse>>(NetworkResult.Empty)
    val userModulesResponseState get() = _userModulesResponseState

    fun fetchAllSalesEntries(employee_id: Int) = viewModelScope.launch {
        _userModulesResponseState.value = NetworkResult.Loading
        try {
            val data = repo.userModules(employee_id)
            _userModulesResponseState.value = NetworkResult.Success(data)
        } catch (e: Throwable) {
            _userModulesResponseState.value = NetworkResult.Error(e)
        }
    }

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


}

